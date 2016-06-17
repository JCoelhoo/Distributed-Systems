package pt.upa.transporter.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.registry.JAXRException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.Handler;

import example.ws.handler.HeaderHandler;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.ca.ws.exception.CAClientException;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
import pt.upa.transporter.ws.exception.TransporterClientException;

public class TransporterClient {

	private boolean verbose = false;

	TransporterService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	TransporterPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS endpoint address */
	private String wsURL = null;

	public TransporterClient(String wsURL) throws TransporterClientException, CertificateException, CAClientException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
	    this.wsURL = wsURL;
		createStub();
	}

	private void uddiLookup() throws TransporterClientException, JAXRException {
		try {
			UDDINaming uddi = new UDDINaming(uddiURL);
			this.wsURL = uddi.lookup(this.wsName);
		} catch (JAXRException exception) {
			throw new TransporterClientException(String.format("Client failed lookup on UDDI at %s!", uddiURL),
					exception);
		}
		if (this.wsURL == null) {
			throw new TransporterClientException(
					String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL));
		}
	}

	public TransporterClient(String uddiURL, String wsName) throws TransporterClientException, JAXRException, CertificateException, CAClientException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
	    this.uddiURL = uddiURL;
		this.wsName = wsName;
		uddiLookup();
		createStub();
	}

	public void createStub() throws CertificateException, CAClientException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new TransporterService();
		port = service.getTransporterPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
	        BindingProvider bindingProvider = (BindingProvider) port;
	        Map<String, Object> requestContext = bindingProvider.getRequestContext();
	        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
		

        HeaderHandler.CONTEXT_PROPERTY = "UpaBroker";
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		return port.requestJob(origin, destination, price);
	}

	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		return port.decideJob(id, accept);
	}

	public JobView jobStatus(String id) {
		return port.jobStatus(id);
	}

	public String ping(String name) {
		return port.ping(name);
	}

	public List<JobView> listJobs() {
		return port.listJobs();
	}

	public void clearJobs() {
		port.clearJobs();
	}

	public boolean isPortSet() {
		return port != null;
	}
}
