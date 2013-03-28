/**
 * 
 */
package nova.compute;

/**
 * Value object for keystone service catalog.
 * @author shida
 *
 */
public class ServiceCatalog {

	private String region;
	private String endpoint;
	
	/**
	 * Constructor.
	 * @param region
	 * @param endpoint
	 */
	public ServiceCatalog(String region, String endpoint) {
		super();
		this.region = region;
		this.endpoint = endpoint;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
}
