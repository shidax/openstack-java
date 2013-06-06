package nova.compute.model;

import java.util.Map;

public class BlockDeviceMapping {

	private String deviceName;
	private String volumeId;
	private Boolean deleteOnTermination;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public Boolean getDeleteOnTermination() {
		return deleteOnTermination;
	}

	public void setDeleteOnTermination(Boolean deleteOnTermination) {
		this.deleteOnTermination = deleteOnTermination;
	}

	public static BlockDeviceMapping fromMessage(Map<String, Object> message) {
		BlockDeviceMapping mapping = new BlockDeviceMapping();
		mapping.volumeId = (String) message.get("volume_id");
		mapping.deviceName = (String) message.get("device_name");
		mapping.deleteOnTermination = (Boolean) message
				.get("delete_on_termination");
		return mapping;
	}
}
