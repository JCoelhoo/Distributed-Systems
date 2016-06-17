package pt.upa.ca;

import pt.upa.ca.ws.CAEndpointManager;


public class CAApplication {
	public static void main(String[] args) throws Exception {

		// Check arguments
		if (args.length == 0 || args.length == 2) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + CAApplication.class.getName() + " wsURL OR uddiURL wsName wsURL");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;

		// Create server implementation object, according to options
		CAEndpointManager endpoint = null;
		if (args.length == 1) {
			wsURL = args[0];
			endpoint = new CAEndpointManager(wsURL);
		} else if (args.length >= 3) {
			uddiURL = args[0];
			wsName = args[1];
			wsURL = args[2];
			endpoint = new CAEndpointManager(uddiURL, wsName, wsURL);
			endpoint.setVerbose(true);
		}

		try {
			endpoint.start();
			endpoint.awaitConnections();
		} finally {
			endpoint.stop();
		}
	}

}
