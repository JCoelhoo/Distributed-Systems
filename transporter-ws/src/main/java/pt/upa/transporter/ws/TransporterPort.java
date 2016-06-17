package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import example.ws.handler.HeaderHandler;

@WebService(endpointInterface = "pt.upa.transporter.ws.TransporterPortType")
@HandlerChain(file="/handler-chain.xml")
public class TransporterPort implements TransporterPortType {
    
    @Resource
    private WebServiceContext webServiceContext;

	private List<JobView> jobs;
	private Random rand;
	private long jobId;
	private int id;
	private Timer headingTimer;
	private Timer ongoingTimer;
	private Timer completedTimer;
	
	private final JobStateView[] acceptedJobs =
	    { JobStateView.REJECTED, JobStateView.ACCEPTED, JobStateView.HEADING,
	            JobStateView.ONGOING, JobStateView.COMPLETED };

	private final String[] transporter2Regions = { "Porto", "Braga", "Viana do Castelo", 
	        "Vila Real", "Braganca", "Lisboa", "Leiria", "Santarem", "Castelo Branco",
	        "Coimbra", "Aveiro", "Viseu", "Guarda" };
	
	private final String[] transporter1Regions = { "Setubal", "Evora", "Portalegre",
	        "Beja", "Faro", "Lisboa", "Leiria", "Santarem", "Castelo Branco", "Coimbra",
	        "Aveiro", "Viseu", "Guarda" };

	private TransporterEndpointManager endpoint;

	public TransporterPort(int id) {
		jobs = new ArrayList<JobView>();
		rand = new Random();
		jobId = 1;
		this.id = id;
		ongoingTimer = new Timer();
		headingTimer = new Timer();
		completedTimer = new Timer();
		
        HeaderHandler.CONTEXT_PROPERTY = "UpaTransporter"+id;
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
	    
		System.out.println("Requesting Job...");
		int offer = 0;
		int random = rand.nextInt(price / 2 + 1) + 1;
		checkLocation(origin, destination);
		checkPrice(price);
		int i = 1;
		if (id % 2 == 0) {
			i = -1;
		}
		if (price > 10 && price <= 100) {
			if (price % 2 != 0) {
				System.out.println("Lower price than requested.");
				offer = price - random * i;
			} else {
				System.out.println("Higher price than requested.");
				offer = price + random * i;
			}
		} else if (price <= 10) {
			System.out.println("Lower than 10.");
			offer = price - random;
		} else {
			return null;
		}
		return createJob("UpaTransporter" + id, Long.toString(jobId), origin, destination, offer,
				JobStateView.PROPOSED);
	}

	private void checkPrice(int price) throws BadPriceFault_Exception {
		if (price <= 0) {
			System.out.println("BadPriceException occured");
			BadPriceFault fault = new BadPriceFault();
			fault.setPrice(price);
			throw new BadPriceFault_Exception("Price invalid", fault);
		}

	}

	private void checkLocation(String origin, String destination) throws BadLocationFault_Exception {
		String errorOrigin = "", errorDestination = "";

		if (id % 2 != 0) {
			if (Arrays.asList(transporter1Regions).contains(origin)) {
				if (Arrays.asList(transporter1Regions).contains(destination)) {
					return;
				} else {
					errorDestination = destination;
				}
			} else {
				errorOrigin = origin;
			}
		} else {
			if (Arrays.asList(transporter2Regions).contains(origin)) {
				if (Arrays.asList(transporter2Regions).contains(destination)) {
					return;
				} else {
					errorDestination = destination;
				}
			} else {
				errorOrigin = origin;
			}
		}
		if (origin == null || destination == null){
			BadLocationFault fault = new BadLocationFault();
			fault.setLocation(destination);
			throw new BadLocationFault_Exception("Location null", fault);
		}
		else if (errorDestination.equals(destination)) {
			BadLocationFault fault = new BadLocationFault();
			fault.setLocation(destination);
			throw new BadLocationFault_Exception("Location invalid", fault);
		} else if (errorOrigin.equals(origin)) {
			BadLocationFault fault = new BadLocationFault();
			fault.setLocation(origin);
			throw new BadLocationFault_Exception("Location invalid", fault);
		}
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		JobView job = checkDuplicate(id) ? null : jobStatus(id);
		int headingTime = (1 + rand.nextInt(4)) * 1000;
		int ongoingTime = (1 + rand.nextInt(4)) * 1000;
		int completedTime = (1 + rand.nextInt(4)) * 1000;
		if (job == null) {
			BadJobFault b = new BadJobFault();
			b.setId(id);
			throw new BadJobFault_Exception("Invalid job ID.", b);
		}
		if (accept) {
			job.setJobState(JobStateView.ACCEPTED);
			headingTimer.schedule(new RemindTask(job, JobStateView.HEADING), headingTime);
			ongoingTimer.schedule(new RemindTask(job, JobStateView.ONGOING), headingTime + ongoingTime);
			completedTimer.schedule(new RemindTask(job, JobStateView.COMPLETED), headingTime + ongoingTime + completedTime);
		} else
			job.setJobState(JobStateView.REJECTED);
		return job;
	}
	
	public boolean checkDuplicate(String id) {
	    return listJobs().stream().filter(j -> j.getJobIdentifier().equals(id) && Arrays.asList(acceptedJobs).contains(j.getJobState())).findAny().isPresent();
	}

	class RemindTask extends TimerTask {
		private JobView jobView;
		private JobStateView stateView;

		public RemindTask(JobView job, JobStateView view) {
			jobView = job;
			stateView = view;
		}

		public void run() {
			jobView.setJobState(stateView);
			System.out.println("Job is now " + stateView.toString());
			this.cancel(); // Terminate the timer thread
		}
	}

	@Override
	public JobView jobStatus(String id) {
		System.out.println("Job Status received");
		for (JobView job : listJobs()) {
			if (job.getJobIdentifier().equals(id)) {
				return job;
			}
		}
		return null;
	}

	public JobView createJob(String name, String jobId, String origin, String destination, int offer,
			JobStateView status) {
		System.out.println("Creating Job...");
		JobView job = new JobView();
		job.setCompanyName(name);
		job.setJobIdentifier(jobId);
		job.setJobOrigin(origin);
		job.setJobDestination(destination);
		job.setJobPrice(offer);
		job.setJobState(status);
		jobs.add(job);
		this.jobId += 1;

		return job;
	}

	@Override
	public String ping(String name) {
		System.out.println("Received Ping...");
		return name + id;
	}

	@Override
	public List<JobView> listJobs() {
		return jobs;
	}

	@Override
	public void clearJobs() {
		jobs.clear();
	}

	public List<JobView> getJobs() {
		return jobs;
	}

}
