package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.BrokerClientApplication;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;
import pt.upa.broker.ws.exception.BrokerClientException;

public class BrokerClient {

	private boolean verbose = false;

	BrokerService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	BrokerPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS endpoint address */
	private String wsURL = null;

	public BrokerClient(String wsURL) throws BrokerClientException {
		this.wsURL = wsURL;
		createStub();
	}

	private void uddiLookup() throws JAXRException, BrokerClientException {
		try {
			UDDINaming uddi = new UDDINaming(uddiURL);
			this.wsURL = uddi.lookup(this.wsName);
			System.out.println("Connected to "+wsURL);
		} catch (JAXRException exception) {
			throw new BrokerClientException(String.format("Client failed lookup on UDDI at %s!", uddiURL), exception);
		}
		if (this.wsURL == null) {
			throw new BrokerClientException(
					String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL));
		} 
	}

	public BrokerClient(String uddiURL, String wsName) throws BrokerClientException, JAXRException {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		uddiLookup();
		createStub();
	}

	public void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new BrokerService();
		port = service.getBrokerPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);

			int connectionTimeout = 20000;
			final List<String> CONN_TIME_PROPS = new ArrayList<String>();
			CONN_TIME_PROPS.add("com.sun.xml.ws.connect.timeout");
			CONN_TIME_PROPS.add("com.sun.xml.internal.ws.connect.timeout");
			CONN_TIME_PROPS.add("javax.xml.ws.client.connectionTimeout");
			for (String propName : CONN_TIME_PROPS)
				requestContext.put(propName, connectionTimeout);

			int receiveTimeout = 20000;
			final List<String> RECV_TIME_PROPS = new ArrayList<String>();
			RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
			RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
			RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");
			for (String propName : RECV_TIME_PROPS)
				requestContext.put(propName, 20000);
		}
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		return port.requestTransport(origin, destination, price);
	}

	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		return port.viewTransport(id);
	}

	public String ping(String name) throws Exception {
		return port.ping(name);
	}

	public List<TransportView> listTransports() {
		return port.listTransports();
	}

	public void clearTransports() {
		port.clearTransports();
	}
	
	public void update(int functionId, TransportView transport, String jobId) {
		port.update(functionId, transport, jobId);
	}

}
