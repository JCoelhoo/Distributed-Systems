package pt.upa.ca.ws;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.jws.WebService;


@WebService(endpointInterface = "pt.upa.ca.ws.CAPortType")
public class CAPort implements CAPortType {
	@Override
	public byte[] requestCertificate(String service) throws Exception {
	    String path = "src/main/resources/" + service + "/" + service + ".cer";
	    System.out.println("File sent: " + path);

	    return Files.readAllBytes(Paths.get(path));
	}
}
