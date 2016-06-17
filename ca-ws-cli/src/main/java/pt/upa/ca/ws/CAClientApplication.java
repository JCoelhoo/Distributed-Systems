package pt.upa.ca.ws;

import javax.xml.registry.JAXRException;
import pt.upa.ca.cli.CAClient;
import pt.upa.ca.ws.exception.CAClientException;

public class CAClientApplication {
	
	CAClient client;

	public static void main(String[] args) throws Exception_Exception, CAClientException, JAXRException {
		// Check arguments
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + CAClientApplication.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client
		CAClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new CAClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new CAClient(uddiURL, wsName);
		}

		System.out.println(client.requestCertificate("UpaBroker"));
		
	}

}
