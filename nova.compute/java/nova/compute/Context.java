/**
 * 
 */
package nova.compute;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * Complete clone of openstack request context.
 * @author shida
 *
 */
public class Context {

	@SerializedName("_context_user_id")
	private String userId;
	@SerializedName("_context_project_id")
	private String projectId;
	@SerializedName("_context_roles")
	private List<String> roles;
	@SerializedName("_context_read_deleted")
	private String readDeleted = "no";
	@SerializedName("_context_remote_address")
	private String remoteAddress;
	@SerializedName("_context_timestamp")
	private String timestamp;
	@SerializedName("_context_request_id")
	private String requestId;
	@SerializedName("_context_auth_token")
	private String authToken;
	@SerializedName("_context_service_catalog")	
	private List<ServiceCatalog> serviceCatalog;
	@SerializedName("_context_is_admin")		
	private boolean isAdmin;
	@SerializedName("_context_user_name")	
	private String useName;
	@SerializedName("_context_project_name")	
	private String projectName;
	
	public Context() {
	}

	/**
	 * Constructor.
	 * @param userId
	 * @param projectId
	 * @param roles
	 * @param readDeleted
	 * @param remoteAddress
	 * @param timestamp
	 * @param requestId
	 * @param authToken
	 * @param serviceCatalog
	 */
	public Context(String userId, String projectId, List<String> roles,
			String readDeleted, String remoteAddress, String timestamp,
			String requestId, String authToken,
			List<ServiceCatalog> serviceCatalog) {
		super();
		this.userId = userId;
		this.projectId = projectId;
		this.roles = roles;
		this.readDeleted = readDeleted;
		this.remoteAddress = remoteAddress;
		this.timestamp = timestamp;
		this.requestId = requestId;
		this.authToken = authToken;
		this.serviceCatalog = serviceCatalog;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getReadDeleted() {
		return readDeleted;
	}

	public void setReadDeleted(String readDeleted) {
		this.readDeleted = readDeleted;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public List<ServiceCatalog> getServiceCatalog() {
		return serviceCatalog;
	}

	public void setServiceCatalog(List<ServiceCatalog> serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	public String getUseName() {
		return useName;
	}

	public void setUseName(String useName) {
		this.useName = useName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@SuppressWarnings("unchecked")
	public static Context unpackContext(Map<String, Object> message) {
		Context context = new Context();
		context.userId = (String) message.get("_context_user_id");
		context.projectId = (String) message.get("_context_project_id");
		context.roles = (List<String>) message.get("_context_roles");
		context.readDeleted = (String) message.get("_context_read_deleted");
		context.authToken = (String) message.get("_context_auth_token");
		context.isAdmin = (Boolean) message.get("_context_is_admin");
		context.projectName = (String) message.get("_context_project_name");
		context.remoteAddress = (String) message.get("_context_remote_address");
		context.requestId = (String) message.get("_context_request_id");
		context.timestamp = (String) message.get("_context_timestamp");
		context.useName = (String) message.get("_context_user_name");
		context.userId = (String) message.get("_context_user_id");
		return context;
	}
}
