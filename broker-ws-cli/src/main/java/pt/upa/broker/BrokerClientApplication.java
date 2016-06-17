package pt.upa.broker;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import com.sun.xml.ws.api.databinding.ClientCallBridge;
import com.sun.xml.ws.wsdl.writer.document.Port;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.ws.*;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.transporter.ws.cli.TransporterClient;

import java.net.SocketTimeoutException;
import java.util.*;
import javax.xml.ws.*;

public class BrokerClientApplication {

	private static BrokerClient client;

	public static void main(String[] args) throws Exception {
		// Check arguments
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + BrokerClientApplication.class.getName() + " wsURL OR uddiURL wsName");
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
		BrokerClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new BrokerClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new BrokerClient(uddiURL, wsName);
		}
		String string = "";
		while (true) {
			System.out.print("Enter command: ");
			Scanner scanner = new Scanner(System.in);
			String value = "";
			value = scanner.nextLine();
			try {
				switch (value) {
				case "1":
					try {
						System.out.println(client.ping(""));
					} catch (WebServiceException wse) {
						System.out.println("Connection Error. Attempting to connect to another Broker...");
						Throwable cause = wse.getCause();
						Thread.sleep(2000);
						client = new BrokerClient(uddiURL, wsName);
						System.out.println("Repeating command...");
						System.out.println(client.ping(""));
						if (cause != null && cause instanceof SocketTimeoutException) {
							System.out.println("Something happened");
						}
					}
					break;
				case "2":
					try {
						string = client.requestTransport("Lisboa", "Lisboa", 19);
					} catch (WebServiceException wse) {
						System.out.println("Connection Error. Attempting to connect to another Broker...");
						Throwable cause = wse.getCause();
						Thread.sleep(2000);
						client = new BrokerClient(uddiURL, wsName);
						System.out.println("Repeating command...");
						string = client.requestTransport("Lisboa", "Lisboa", 19);
						if (cause != null && cause instanceof SocketTimeoutException) {
							System.out.println("Something happened");
						}
					}
					break;
				case "3":
					try {
						System.out.println(client.viewTransport(string).getPrice());
					} catch (WebServiceException wse) {
						System.out.println("Connection Error. Attempting to connect to another Broker...");
						Throwable cause = wse.getCause();
						Thread.sleep(2000);
						client = new BrokerClient(uddiURL, wsName);
						System.out.println("Repeating command...");
						System.out.println(client.viewTransport(string).getPrice());
						if (cause != null && cause instanceof SocketTimeoutException) {
							System.out.println("Something happened");
						}
					}
					break;
				case "4":
					try {
						client.clearTransports();
					} catch (WebServiceException wse) {
						System.out.println("Connection Error. Attempting to connect to another Broker...");
						Throwable cause = wse.getCause();
						Thread.sleep(2000);
						client = new BrokerClient(uddiURL, wsName);
						System.out.println("Repeating command...");
						client.clearTransports();
						if (cause != null && cause instanceof SocketTimeoutException) {
							System.out.println("Something happened");
						}
					} catch (Exception e) {
						System.out.println("No Jobs Found");
					}
					break;
				case "5":
					try {
						string = client.requestTransport("Lisboa", "Lisboa", 20);
					} catch (WebServiceException wse) {
						wse.printStackTrace();
					}
					break;
				default:
					System.out.println("Invalid command");
					break;
				}
			} catch (Exception e) {
				System.out.println("Exception");
				e.printStackTrace();
			}
		}
	}

}
