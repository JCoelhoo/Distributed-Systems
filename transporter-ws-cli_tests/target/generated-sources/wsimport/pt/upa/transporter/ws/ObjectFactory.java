
package pt.upa.transporter.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pt.upa.transporter.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ListJobs_QNAME = new QName("http://ws.transporter.upa.pt/", "listJobs");
    private final static QName _Ping_QNAME = new QName("http://ws.transporter.upa.pt/", "ping");
    private final static QName _JobStatus_QNAME = new QName("http://ws.transporter.upa.pt/", "jobStatus");
    private final static QName _DecideJobResponse_QNAME = new QName("http://ws.transporter.upa.pt/", "decideJobResponse");
    private final static QName _DecideJob_QNAME = new QName("http://ws.transporter.upa.pt/", "decideJob");
    private final static QName _ListJobsResponse_QNAME = new QName("http://ws.transporter.upa.pt/", "listJobsResponse");
    private final static QName _BadLocationFault_QNAME = new QName("http://ws.transporter.upa.pt/", "BadLocationFault");
    private final static QName _RequestJobResponse_QNAME = new QName("http://ws.transporter.upa.pt/", "requestJobResponse");
    private final static QName _PingResponse_QNAME = new QName("http://ws.transporter.upa.pt/", "pingResponse");
    private final static QName _BadPriceFault_QNAME = new QName("http://ws.transporter.upa.pt/", "BadPriceFault");
    private final static QName _ClearJobs_QNAME = new QName("http://ws.transporter.upa.pt/", "clearJobs");
    private final static QName _ClearJobsResponse_QNAME = new QName("http://ws.transporter.upa.pt/", "clearJobsResponse");
    private final static QName _BadJobFault_QNAME = new QName("http://ws.transporter.upa.pt/", "BadJobFault");
    private final static QName _RequestJob_QNAME = new QName("http://ws.transporter.upa.pt/", "requestJob");
    private final static QName _JobStatusResponse_QNAME = new QName("http://ws.transporter.upa.pt/", "jobStatusResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pt.upa.transporter.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JobStatus }
     * 
     */
    public JobStatus createJobStatus() {
        return new JobStatus();
    }

    /**
     * Create an instance of {@link DecideJobResponse }
     * 
     */
    public DecideJobResponse createDecideJobResponse() {
        return new DecideJobResponse();
    }

    /**
     * Create an instance of {@link ListJobs }
     * 
     */
    public ListJobs createListJobs() {
        return new ListJobs();
    }

    /**
     * Create an instance of {@link Ping }
     * 
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link BadLocationFault }
     * 
     */
    public BadLocationFault createBadLocationFault() {
        return new BadLocationFault();
    }

    /**
     * Create an instance of {@link DecideJob }
     * 
     */
    public DecideJob createDecideJob() {
        return new DecideJob();
    }

    /**
     * Create an instance of {@link ListJobsResponse }
     * 
     */
    public ListJobsResponse createListJobsResponse() {
        return new ListJobsResponse();
    }

    /**
     * Create an instance of {@link RequestJobResponse }
     * 
     */
    public RequestJobResponse createRequestJobResponse() {
        return new RequestJobResponse();
    }

    /**
     * Create an instance of {@link ClearJobs }
     * 
     */
    public ClearJobs createClearJobs() {
        return new ClearJobs();
    }

    /**
     * Create an instance of {@link ClearJobsResponse }
     * 
     */
    public ClearJobsResponse createClearJobsResponse() {
        return new ClearJobsResponse();
    }

    /**
     * Create an instance of {@link BadJobFault }
     * 
     */
    public BadJobFault createBadJobFault() {
        return new BadJobFault();
    }

    /**
     * Create an instance of {@link RequestJob }
     * 
     */
    public RequestJob createRequestJob() {
        return new RequestJob();
    }

    /**
     * Create an instance of {@link JobStatusResponse }
     * 
     */
    public JobStatusResponse createJobStatusResponse() {
        return new JobStatusResponse();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link BadPriceFault }
     * 
     */
    public BadPriceFault createBadPriceFault() {
        return new BadPriceFault();
    }

    /**
     * Create an instance of {@link JobView }
     * 
     */
    public JobView createJobView() {
        return new JobView();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListJobs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "listJobs")
    public JAXBElement<ListJobs> createListJobs(ListJobs value) {
        return new JAXBElement<ListJobs>(_ListJobs_QNAME, ListJobs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JobStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "jobStatus")
    public JAXBElement<JobStatus> createJobStatus(JobStatus value) {
        return new JAXBElement<JobStatus>(_JobStatus_QNAME, JobStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DecideJobResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "decideJobResponse")
    public JAXBElement<DecideJobResponse> createDecideJobResponse(DecideJobResponse value) {
        return new JAXBElement<DecideJobResponse>(_DecideJobResponse_QNAME, DecideJobResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DecideJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "decideJob")
    public JAXBElement<DecideJob> createDecideJob(DecideJob value) {
        return new JAXBElement<DecideJob>(_DecideJob_QNAME, DecideJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListJobsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "listJobsResponse")
    public JAXBElement<ListJobsResponse> createListJobsResponse(ListJobsResponse value) {
        return new JAXBElement<ListJobsResponse>(_ListJobsResponse_QNAME, ListJobsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BadLocationFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "BadLocationFault")
    public JAXBElement<BadLocationFault> createBadLocationFault(BadLocationFault value) {
        return new JAXBElement<BadLocationFault>(_BadLocationFault_QNAME, BadLocationFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestJobResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "requestJobResponse")
    public JAXBElement<RequestJobResponse> createRequestJobResponse(RequestJobResponse value) {
        return new JAXBElement<RequestJobResponse>(_RequestJobResponse_QNAME, RequestJobResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BadPriceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "BadPriceFault")
    public JAXBElement<BadPriceFault> createBadPriceFault(BadPriceFault value) {
        return new JAXBElement<BadPriceFault>(_BadPriceFault_QNAME, BadPriceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearJobs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "clearJobs")
    public JAXBElement<ClearJobs> createClearJobs(ClearJobs value) {
        return new JAXBElement<ClearJobs>(_ClearJobs_QNAME, ClearJobs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearJobsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "clearJobsResponse")
    public JAXBElement<ClearJobsResponse> createClearJobsResponse(ClearJobsResponse value) {
        return new JAXBElement<ClearJobsResponse>(_ClearJobsResponse_QNAME, ClearJobsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BadJobFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "BadJobFault")
    public JAXBElement<BadJobFault> createBadJobFault(BadJobFault value) {
        return new JAXBElement<BadJobFault>(_BadJobFault_QNAME, BadJobFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "requestJob")
    public JAXBElement<RequestJob> createRequestJob(RequestJob value) {
        return new JAXBElement<RequestJob>(_RequestJob_QNAME, RequestJob.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JobStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.transporter.upa.pt/", name = "jobStatusResponse")
    public JAXBElement<JobStatusResponse> createJobStatusResponse(JobStatusResponse value) {
        return new JAXBElement<JobStatusResponse>(_JobStatusResponse_QNAME, JobStatusResponse.class, null, value);
    }

}
