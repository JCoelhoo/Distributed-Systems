package pt.upa.ca.ws;

import javax.jws.WebService;

@WebService
public interface CAPortType {
	public byte[] requestCertificate(String service) throws Exception;
}
