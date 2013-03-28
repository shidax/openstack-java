/**
 * 
 */
package nova.compute;

/**
 * @author shida
 * 
 */
public class ComputeConfig {

	private String rabbitmqHost;
	private String rabbitmqVirtualHost;
	private String rabbitmqUserName;
	private String rabbitmqPassword;
	private String host;
	private String serviceUser;
	private String serviceProject;
	private String servicePassword;
	private long period;
	private String glanceEndpoint;
	private String instanceDirectory = "instances";
	private String vboxUser;
	private String vboxPassword;
	private String vboxUrl;
	
	public String getVboxUser() {
		return vboxUser;
	}

	public void setVboxUser(String vboxUser) {
		this.vboxUser = vboxUser;
	}

	public String getVboxPassword() {
		return vboxPassword;
	}

	public void setVboxPassword(String vboxPassword) {
		this.vboxPassword = vboxPassword;
	}

	public String getVboxUrl() {
		return vboxUrl;
	}

	public void setVboxUrl(String vboxUrl) {
		this.vboxUrl = vboxUrl;
	}

	public String getInstanceDirectory() {
		return instanceDirectory;
	}

	public void setInstanceDirectory(String instanceDirectory) {
		this.instanceDirectory = instanceDirectory;
	}

	public String getGlanceEndpoint() {
		return glanceEndpoint;
	}

	public void setGlanceEndpoint(String glanceEndpoint) {
		this.glanceEndpoint = glanceEndpoint;
	}

	public String getRabbitmqHost() {
		return rabbitmqHost;
	}

	public void setRabbitmqHost(String rabbitmqHost) {
		this.rabbitmqHost = rabbitmqHost;
	}

	public String getRabbitmqVirtualHost() {
		return rabbitmqVirtualHost;
	}

	public void setRabbitmqVirtualHost(String rabbitmqVirtualHost) {
		this.rabbitmqVirtualHost = rabbitmqVirtualHost;
	}

	public String getRabbitmqUserName() {
		return rabbitmqUserName;
	}

	public void setRabbitmqUserName(String rabbitmqUserName) {
		this.rabbitmqUserName = rabbitmqUserName;
	}

	public String getRabbitmqPassword() {
		return rabbitmqPassword;
	}

	public void setRabbitmqPassword(String rabbitmqPassword) {
		this.rabbitmqPassword = rabbitmqPassword;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getServiceUser() {
		return serviceUser;
	}

	public void setServiceUser(String serviceUser) {
		this.serviceUser = serviceUser;
	}

	public String getServiceProject() {
		return serviceProject;
	}

	public void setServiceProject(String serviceProject) {
		this.serviceProject = serviceProject;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
}
