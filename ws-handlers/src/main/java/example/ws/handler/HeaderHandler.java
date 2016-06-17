package example.ws.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.NodeList;

import pt.upa.ca.cli.CAClient;
import pt.upa.ca.ws.Exception_Exception;
import pt.upa.ca.ws.exception.CAClientException;

/**
 * This SOAPHandler shows how to set/get values from headers in inbound/outbound
 * SOAP messages.
 *
 * A header is created in an outbound message and is read on an inbound message.
 *
 * The value that is read from the header is placed in a SOAP message context
 * property that can be accessed by other handlers or by the application.
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

	private PublicKey kpCA = null;
	private PrivateKey ksMY = null;
	
	private ArrayList<String> noncesOutbound = new ArrayList<String>();
    private ArrayList<String> noncesInbound = new ArrayList<String>();
    
    private CAClient client = null;
    
	public static String CONTEXT_PROPERTY = null;
	
    final static String KEYSTORE_PASSWORD = "1nsecure";
    final static String KEY_PASSWORD = "ins3cur3";

	public HeaderHandler() 
	        throws CertificateException, CAClientException, IOException, UnrecoverableKeyException,
	            NoSuchAlgorithmException, KeyStoreException {

        client = new CAClient("http://localhost:8090/ca-ws/endpoint");	
	    String path = "src/main/resources/ca-certificate.pem";
        byte[] arr = Files.readAllBytes(Paths.get(path));   
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        kpCA = cf.generateCertificate(bis).getPublicKey();
	}
	
	//
	// Handler interface methods
	//
	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		System.out.println("AddHeaderHandler: Handling message.");
		Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
        try {
            if(ksMY == null) setContext();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
		
		try {
			if (outboundElement.booleanValue()) {
				System.out.println("Writing header in outbound SOAP message...");

				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();

				// add header
				SOAPHeader sh = se.getHeader();
				SOAPBody sb = se.getBody();
				if (sh == null)
					sh = se.addHeader();
				if (sb == null) {
					sb = se.addBody();
				}
				
				// add header element (name, namespace prefix, namespace)
				Name nonce = se.createName("Nonce", "n", "http://nonce");
				SOAPHeaderElement element = sh.addHeaderElement(nonce);

				// add header element value
		        String nonceString = newNonce();
				element.addTextNode(nonceString);

				Name ent = se.createName("Ent", "e", "http://ent");
				SOAPHeaderElement entElement = sh.addHeaderElement(ent);

				// add header element value
                entElement.addTextNode(CONTEXT_PROPERTY);
				
				// add header element (name, namespace prefix, namespace)
				Name digTag = se.createName("Digest", "d", "http://digest");
				SOAPHeaderElement dig = sh.addHeaderElement(digTag);
				
		        Signature sig = Signature.getInstance("SHA1WithRSA");
		        sig.initSign(ksMY);
		        sig.update(computeDigest(sb, nonceString));
				dig.addTextNode(printBase64Binary(sig.sign()));
                
                msg.saveChanges();
                return true;

			} else {
				System.out.println("Reading header in inbound SOAP message...");
				


				
				if (getEnvelope(smc).getHeader() == null){
		            System.out.println("----->Header not found.\n");
		            return false;
		        }

				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				
				SOAPBody sbd = smc.getMessage().getSOAPBody(); 
                SOAPElement ent = getElementFromHeader(smc, "Ent", "e", "http://ent");
                SOAPElement non = getElementFromHeader(smc, "Nonce", "n", "http://nonce");
                SOAPElement dig = getElementFromHeader(smc, "Digest", "d", "http://digest");
		        if(!areSOAPElementsValid(sbd, non, ent, dig)) return false;
		        
				if(sbd.getTextContent().equals("LisboaLisboa20")){
					System.out.println("\n-------I AM THE MAN IN THE MIDDLE---------\n");
			        se = getEnvelope(smc);
			        Name name = se.createName("requestJob", "ns2", "http://ws.transporter.upa.pt/");
			        Name n = se.createName("price");
			        Iterator it = se.getBody().getChildElements(name);
			        SOAPElement s = (SOAPElement) it.next();
			        Iterator it2 = s.getChildElements(n);
			        SOAPElement s2 = (SOAPElement) it2.next();
			        s2.setTextContent("40");   
				}
		        
		        String nonString = non.getValue();
		        String entString = ent.getValue();
		        Certificate cer = getCertificate(client.requestCertificate(entString));
		        		        
                byte[] senderDig = parseBase64Binary(dig.getValue());
                byte[] digest = computeDigest(sbd, nonString);
                if(!verifyDigitalSignature(senderDig, digest, cer.getPublicKey())) {
                    System.out.println("---->Digest doesnt match body digest\n");
                    return false;                    
                }
                
                if(noncesInbound.contains(nonString)) {
                    System.out.println("----->Invalid Nonce\n");
                    return false;
                }
                noncesInbound.add(nonString);
                
                return true;
				
			}
		} catch (Exception e) {
			System.out.print("Caught exception in handleMessage: ");
			System.out.println(e);
			System.out.println("Continue normal processing...");
		}
        System.out.println("---->Failed to verify SOAP\n");
		return false;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		System.out.println("Ignoring fault message...");
		return true;
	}

	public void close(MessageContext messageContext) {
	}
	
	public String newNonce() throws NoSuchAlgorithmException {
	    final byte array[] = new byte[16];
	    String nonce = null;
	    
	    while(nonce == null || noncesOutbound.contains(nonce)) {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(array);
            nonce = printBase64Binary(array);
	    }
        noncesOutbound.add(nonce);
        
        return nonce;        
	}
	
	public SOAPEnvelope getEnvelope(SOAPMessageContext smc) throws SOAPException {
	    return smc.getMessage().getSOAPPart().getEnvelope();
	}
	
	public SOAPElement getElementFromHeader(SOAPMessageContext smc, String localName, String prefix, String uri) throws SOAPException {
        SOAPEnvelope se = getEnvelope(smc);
        Name name = se.createName(localName, prefix, uri);
        Iterator it = se.getHeader().getChildElements(name);
        
        // check header element
        if (!it.hasNext()) {
            System.out.println("Header element not found" + localName);
            return null;
        }
        
        return (SOAPElement) it.next();
	}
	
	public boolean areSOAPElementsValid(SOAPBody sbd, SOAPElement non, SOAPElement cer, SOAPElement dig) {
	    if (sbd == null) {
            System.out.println("----->Body not found.");
            return false;             
	    } else if (non == null) {
            System.out.println("----->Nonce not found.");
            return false;                   
        } else if (cer == null) {
            System.out.println("----->Certificate not found.");
            return false;                   
        } else if (dig == null) {
            System.out.println("----->Digest not found.");
            return false;                   
        }
        
        return true;
	}
	
	public byte[] decipherElement(SOAPElement element, PublicKey key)
	        throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
	    
	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] ciphered = element.getValue().getBytes();
        byte[] deciphered = cipher.doFinal(ciphered);
        return deciphered;
	}
	
	public Certificate getCertificate(byte[] cer) 
	        throws CAClientException, CertificateException, Exception_Exception, InvalidKeyException, 
	            NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException {
	    
	    CertificateFactory cf = CertificateFactory.getInstance("X.509"); 
        ByteArrayInputStream bis = new ByteArrayInputStream(cer);
        Certificate certificate = cf.generateCertificate(bis);
        certificate.verify(kpCA);
        return certificate;
	}
	
	public byte[] computeDigest(SOAPBody sbd, String nonce) throws NoSuchAlgorithmException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
	    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
	    DOMSource source = new DOMSource(sbd);
	    StringWriter stringResult = new StringWriter();
	    TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
	    String message = stringResult.toString() + nonce;
	    messageDigest.update(message.getBytes());   
        return messageDigest.digest();	    
	}
	
	public void p(String m) {
	    System.out.println(m);
	}
	
	public void setContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {

        String path = "src/main/resources/" + CONTEXT_PROPERTY + ".jks";
        FileInputStream fis = new FileInputStream(path);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(fis, KEY_PASSWORD.toCharArray());        
        ksMY = (PrivateKey) keystore.getKey(CONTEXT_PROPERTY, KEYSTORE_PASSWORD.toCharArray());
	}
	
   public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
            throws Exception {

        // verify the signature with the public key
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(publicKey);
        sig.update(bytes);
        try {
            return sig.verify(cipherDigest);
        } catch (SignatureException se) {
            System.err.println("----->Caught exception while verifying signature " + se);
            return false;
        }
    }
}