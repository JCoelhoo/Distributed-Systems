package pt.upa.broker.ws;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;
import javax.xml.ws.WebServiceException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.broker.ws.exception.BrokerClientException;
import pt.upa.ca.ws.exception.CAClientException;
import pt.upa.transporter.ws.*;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.exception.TransporterClientException;

@WebService(endpointInterface = "pt.upa.broker.ws.BrokerPortType")
public class BrokerPort implements BrokerPortType {

	private Map<String, TransportView> transportList;
	private Map<String, TransporterClient> clientList;
	private TransporterClient client;
	private BrokerClient backupBroker = null, broker = null;
	private boolean backup;
	BrokerEndpointManager endpoint = null;

	private final static int CLEAR_FUNC = 3;
	private final static int REQUEST_FUNC = 1;
	private final static int VIEW_FUNC = 2;

	private long id = 0;
	String string;

	private final String[] transporterRegions = { "Porto", "Braga", "Viana do Castelo", "Vila Real", "Braganca",
			"Lisboa", "Leiria", "Santarem", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda", "Setubal",
			"Evora", "Portalegre", "Beja", "Faro" };

	public BrokerPort(boolean b) throws TransporterClientException, JAXRException, BrokerClientException, CertificateException, CAClientException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
		UDDINaming uddi = new UDDINaming("http://localhost:9090");
		backupBroker = new BrokerClient("http://localhost:8091/broker-ws/endpoint");
		broker = new BrokerClient("http://localhost:8080/broker-ws/endpoint");
		transportList = new HashMap<String, TransportView>();
		clientList = new HashMap<String, TransporterClient>();
		backup = b;

		for (int i = 1; i < 3; i++) {
			String url = uddi.lookup("UpaTransporter" + i);
			if (url != null) {
				client = new TransporterClient(url);
				clientList.put("UpaTransporter" + i, client);
			}
		}		
		
		if (backup) {
			Timer timer = new Timer();
			timer.schedule(new RemindTask(), 2 * 1000);
			
		}
	}

	class RemindTask extends TimerTask {

		public void run() {
			try {
				proofOfLife();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Requesting proof of life...");
			Timer timer = new Timer();
			timer.schedule(new RemindTask(), 3 * 1000);
			this.cancel(); // Terminate the timer thread
		}
	}

	// used for testing
	public BrokerPort(Map<String, TransporterClient> clients) {
		clientList = new HashMap<String, TransporterClient>(clients);
		transportList = new HashMap<String, TransportView>();
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws UnavailableTransportPriceFault_Exception, UnavailableTransportFault_Exception,
			InvalidPriceFault_Exception, UnknownLocationFault_Exception {

		List<JobView> tempJobViews = new ArrayList<JobView>();
		TransportView transportView = new TransportView();
		JobView jobViewLowestPrice = null;
		boolean badLocation = true;

		System.out.println("Transport requested...");

		if (price <= 0) {
			transportView.setState(TransportStateView.FAILED);
			String s = transportId();
			transportList.put(s, transportView);
			if (!backup)
				backupBroker.update(REQUEST_FUNC, transportView, s);
			throw new InvalidPriceFault_Exception("Price invalid", null);
		} else if (price > 100) {
			transportView.setState(TransportStateView.FAILED);
			String s = transportId();
			transportList.put(s, transportView);
			if (!backup)
				backupBroker.update(REQUEST_FUNC, transportView, s);
			throw new UnavailableTransportFault_Exception("Unknown Location", null);
		}

		if (!Arrays.asList(transporterRegions).contains(origin)
				|| !Arrays.asList(transporterRegions).contains(destination)) {
			transportView.setState(TransportStateView.FAILED);
			String s = transportId();
			transportList.put(s, transportView);
			if (!backup)
				backupBroker.update(REQUEST_FUNC, transportView, s);
			throw new UnknownLocationFault_Exception("Unknown Location", null);
		}

		transportView.setOrigin(origin);
		transportView.setDestination(destination);
		for (TransporterClient client : clientList.values()) {
			JobView jobView = null;
			try {
				jobView = client.requestJob(origin, destination, price);
				badLocation = false;
				if (jobView != null) {
					tempJobViews.add(jobView);
				}
			} catch (BadLocationFault_Exception e) {
				System.out.println("Bad Location Fault");
			} catch (BadPriceFault_Exception e) {
				e.printStackTrace();
			}

		}

		if (tempJobViews.size() == 0) {
			transportView.setState(TransportStateView.FAILED);
			String s = transportId();
			transportList.put(s, transportView);
			if (!backup)
				backupBroker.update(REQUEST_FUNC, transportView, s);
			throw new UnavailableTransportFault_Exception("Unknown Location", null);
		}

		transportView.setState(TransportStateView.REQUESTED);

		jobViewLowestPrice = tempJobViews.get(0);

		for (JobView jobView : tempJobViews) {
			if (jobView.getJobPrice() <= price) {
				transportView.setState(TransportStateView.BUDGETED);
				if (jobView.getJobPrice() < jobViewLowestPrice.getJobPrice()) {
					jobViewLowestPrice = jobView;
				}
			}
		}

		if (transportView.getState() == TransportStateView.REQUESTED) {
			UnavailableTransportPriceFault tf = new UnavailableTransportPriceFault();
			tf.setBestPriceFound(jobViewLowestPrice.getJobPrice());
			throw new UnavailableTransportPriceFault_Exception("Unavailable Price", tf);
		}

		string = jobViewLowestPrice.getCompanyName();

		System.out.println("Offer: " + string);
		transportView.setState(TransportStateView.BOOKED);

		try {
			for (JobView jobView : tempJobViews) {
				if (jobViewLowestPrice.equals(jobView)) {
					transportView.setPrice(jobViewLowestPrice.getJobPrice());
					clientList.get(jobView.getCompanyName()).decideJob(jobViewLowestPrice.getJobIdentifier(), true);
					transportView.setTransporterCompany(jobViewLowestPrice.getCompanyName());
				} else {
					clientList.get(jobView.getCompanyName()).decideJob(jobView.getJobIdentifier(), false);
				}
			}
		} catch (BadJobFault_Exception e) {
			UnavailableTransportFault tf = new UnavailableTransportFault();
			tf.setDestination(destination);
			tf.setOrigin(origin);
			throw new UnavailableTransportFault_Exception("Unknown Transport", tf);
		}

		transportView.setId(jobViewLowestPrice.getJobIdentifier());
		String s = transportId();
		transportList.put(s, transportView);
		if (!backup)
			backupBroker.update(REQUEST_FUNC, transportView, s);
		return Long.toString(id);
	}

	public String transportId() {
		id += 1;
		return Long.toString(id);
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		TransportView transportView = transportList.get(id);
		if (transportView == null) {
			throw new UnknownTransportFault_Exception("Unknown Transport", null);
		}
		if (transportView.getState() != TransportStateView.FAILED) {

			TransporterClient client = clientList.get(transportView.getTransporterCompany());
			if (client == null) {
				throw new UnknownTransportFault_Exception("Unknown Client", null);
			}

			JobView jobview = client.jobStatus(transportView.getId());

			if (jobview.getJobState().equals(JobStateView.HEADING)) {
				transportView.setState(TransportStateView.HEADING);
			} else if (jobview.getJobState().equals(JobStateView.ONGOING)) {
				transportView.setState(TransportStateView.ONGOING);
			} else if (jobview.getJobState().equals(JobStateView.COMPLETED)) {
				transportView.setState(TransportStateView.COMPLETED);
			}

		}

		if (!backup)
			backupBroker.update(VIEW_FUNC, transportView, id);
		return transportView;
	}

	@Override
	public String ping(String name) {
		// TEST
		if (name.equals("proofOfLife")){
			System.out.println("Sending proof of life...");
			return "I'm alive";
		}
		System.out.println("Received ping...");
		String string = name + "You have reached the Broker";
		for (TransporterClient client : clientList.values()) {
			string += client.ping("\nYou have reached Transporter");
		} 
		return string;
	}

	@Override
	public List<TransportView> listTransports() {
		List<TransportView> list = new ArrayList<TransportView>(transportList.values());
		for (TransportView t: list){
			System.out.println("--------------------------");
			System.out.println("Destination: "+t.getDestination());
			System.out.println("Origin: "+t.getOrigin());
			System.out.println("--------------------------");
		}
		return list;
	}

	@Override
	public void clearTransports() {
		System.out.println("Clearing Transports...");
		for (TransporterClient transporterClient : clientList.values()) {
			transporterClient.clearJobs();
		}
		settViews(new HashMap<String, TransportView>());
		if (!backup)
			backupBroker.update(CLEAR_FUNC, null, null);
	}

	public Map<String, TransportView> gettViews() {
		return transportList;
	}

	public void settViews(Map<String, TransportView> t) {
		transportList = t;
	}

	public void proofOfLife() throws Exception {
		int connectionTimeout = 2000;
		final List<String> CONN_TIME_PROPS = new ArrayList<String>();
		CONN_TIME_PROPS.add("com.sun.xml.ws.connect.timeout");
		CONN_TIME_PROPS.add("com.sun.xml.internal.ws.connect.timeout");
		CONN_TIME_PROPS.add("javax.xml.ws.client.connectionTimeout");

		int receiveTimeout = 3000;
		final List<String> RECV_TIME_PROPS = new ArrayList<String>();
		RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
		RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
		RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");

		try {
			broker.ping("proofOfLife");
		} catch (WebServiceException wse) {
			System.out.println("Main Broker has died. Backup starting...");
			Throwable cause = wse.getCause();
			backup = !backup;
			endpoint = new BrokerEndpointManager("http://localhost:9090", "UpaBroker", "http://localhost:8091/broker-ws/endpoint", false);
			endpoint.publishToUDDI();
			endpoint.awaitConnections();
			if (cause != null && cause instanceof SocketTimeoutException) {
				System.out.println("The cause was a timeout exception: " + cause);

			}
		}
	}

	@Override
	public void update(Integer functionId, TransportView transport, String jobId) {
		System.out.println("Updating...");
		switch (functionId) {
		case 1:
			System.out.println("Replicating Transport Request...");
			transportList.put(jobId, transport);
			break;
		case 2:
			System.out.println("Replicating Transport View...");
			transportList.put(jobId, transport);
			break;
		case 3:
			System.out.println("Replicating Clear Request...");
			settViews(new HashMap<String, TransportView>());
			break;
		default:
			break;
		}
	}

}
