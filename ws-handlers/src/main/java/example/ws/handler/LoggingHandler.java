package example.ws.handler;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * This SOAPHandler outputs the contents of inbound and outbound messages.
 */
public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        logToSystemOut(smc);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        logToSystemOut(smc);
        return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
    }

    /**
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this is an
     * outgoing or incoming message. Write a brief message to the print stream
     * and output the message. The writeTo() method can throw SOAPException or
     * IOException
     */
    private void logToSystemOut(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outbound) {
            System.out.println("Outbound SOAP message:");
        } else {
            System.out.println("Inbound SOAP message:");
        }

        SOAPMessage message = smc.getMessage();
        try {
            System.out.println(getSOAPMessageAsString(message)); // just to add a newline to output
        } catch (Exception e) {
            System.out.printf("Exception in handler: %s%n", e);
        }
    }
    
    public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
        try {

           TransformerFactory tff = TransformerFactory.newInstance();
           Transformer tf = tff.newTransformer();

           // Set formatting
          
           tf.setOutputProperty(OutputKeys.INDENT, "yes");
           tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                 "2");
           
           Source sc = soapMessage.getSOAPPart().getContent();

           ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
           StreamResult result = new StreamResult(streamOut);
           tf.transform(sc, result);

           String strMessage = streamOut.toString();
           return strMessage;
        } catch (Exception e) {
           System.out.println("Exception in getSOAPMessageAsString "
                 + e.getMessage());
           return null;
        }

     }

}
