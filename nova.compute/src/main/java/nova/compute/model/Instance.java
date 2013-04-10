/**
 * 
 */
package nova.compute.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static nova.compute.model.ModelUtil.toLong;
import static nova.compute.model.ModelUtil.toInt;

/**
 * @author shida
 * 
 */
public class Instance {

	private String uuid;
	private String vmState;
	private String availabilityZone;
	private Long ephemeralGb;
	private Long rootGb;
	private Long memoryMb;
	private Long vcpus;
	private String userData;
	private String userId;
	private String hostName;
	private String displayDescription;
	private String keyData;
	private Integer powerState;
	private String projectId;
	private String configDrive;
	private String keyName;
	private String displayName;
	private String taskState;
	private Boolean shutdownTerminate;
	private String name;
	private Image image;
	private List<BlockDeviceMapping> blockDeviceMappings = new ArrayList<BlockDeviceMapping>();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVmState() {
		return vmState;
	}

	public void setVmState(String vmState) {
		this.vmState = vmState;
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public Long getEphemeralGb() {
		return ephemeralGb;
	}

	public void setEphemeralGb(Long ephemeralGb) {
		this.ephemeralGb = ephemeralGb;
	}

	public Long getRootGb() {
		return rootGb;
	}

	public void setRootGb(Long rootGb) {
		this.rootGb = rootGb;
	}

	public Long getMemoryMb() {
		return memoryMb;
	}

	public void setMemoryMb(Long memoryMb) {
		this.memoryMb = memoryMb;
	}

	public Long getVcpus() {
		return vcpus;
	}

	public void setVcpus(Long vcpus) {
		this.vcpus = vcpus;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public String getKeyData() {
		return keyData;
	}

	public void setKeyData(String keyData) {
		this.keyData = keyData;
	}

	public Integer getPowerState() {
		return powerState;
	}

	public void setPowerState(Integer powerState) {
		this.powerState = powerState;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getConfigDrive() {
		return configDrive;
	}

	public void setConfigDrive(String configDrive) {
		this.configDrive = configDrive;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public Boolean getShutdownTerminate() {
		return shutdownTerminate;
	}

	public void setShutdownTerminate(Boolean shutdownTerminate) {
		this.shutdownTerminate = shutdownTerminate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<BlockDeviceMapping> getBlockDeviceMappings() {
		return blockDeviceMappings;
	}

	public void setBlockDeviceMappings(
			List<BlockDeviceMapping> blockDeviceMappings) {
		this.blockDeviceMappings = blockDeviceMappings;
	}

	public static final Instance fromMessage(Map<String, Object> value) {
		Instance instance = new Instance();
		instance.uuid = (String) value.get("uuid");
		instance.vmState = (String) value.get("vm_state");
		instance.availabilityZone = (String) value.get("availability_zone");
		instance.ephemeralGb = toLong(value.get("ephemeral_gb"));
		instance.rootGb = toLong(value.get("root_gb"));
		instance.memoryMb = toLong(value.get("memory_mb"));
		instance.vcpus = toLong(value.get("vcpus"));
		instance.userData = (String) value.get("userData");
		instance.userId = (String) value.get("user_id");
		instance.hostName = (String) value.get("hostname");
		instance.displayDescription = (String) value.get("display_description");
		instance.keyData = (String) value.get("key_data");
		instance.powerState = toInt(value.get("power_state"));
		instance.projectId = (String) value.get("project_id");
		instance.configDrive = (String) value.get("config_drive");
		instance.keyName = (String) value.get("key_name");
		instance.displayName = (String) value.get("display_name");
		instance.taskState = (String) value.get("task_state");
		instance.shutdownTerminate = (Boolean) value.get("shutdown_terminate");
		instance.name = (String) value.get("name");
		return instance;
	}
}
