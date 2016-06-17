package pt.upa.ca.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.ca.ws.CAPortService;
import pt.upa.ca.ws.CAPortType;
import pt.upa.ca.ws.Exception_Exception;
import pt.upa.ca.ws.exception.CAClientException;

public class CAClient 
{
	private boolean verbose = false;

	CAPortService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	CAPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS endpoint address */
	private String wsURL = null;

	public CAClient(String wsURL) throws CAClientException {
		this.wsURL = wsURL;
		createStub();
	}

	private void uddiLookup() throws JAXRException, CAClientException {
		try {
			UDDINaming uddi = new UDDINaming(uddiURL);
			this.wsURL = uddi.lookup(this.wsName);
		} catch (JAXRException exception) { 
		throw new CAClientException(String.format(
                "Client failed lookup on UDDI at %s!", uddiURL), exception);}
		if (this.wsURL == null) { 
			throw new CAClientException(
					String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL));
		}
	}

	public CAClient(String uddiURL, String wsName) throws CAClientException, JAXRException {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		uddiLookup();
		createStub();
	}

	public void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new CAPortService();
		port = service.getCAPortPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public byte[] requestCertificate(String string) throws Exception_Exception {
		return port.requestCertificate(string);
	}
}
