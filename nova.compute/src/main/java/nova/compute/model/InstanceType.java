/**
 * 
 */
package nova.compute.model;

import java.util.Map;
import static nova.compute.model.ModelUtil.*;

/**
 * @author shida
 * 
 */
public class InstanceType {

	private Long rootGb;
	private String flavorId;
	private String name;
	private Long memoryMb;
	private Long vcpus;
	private Long ephemeralGb;
	private Long swap;

	public Long getRootGb() {
		return rootGb;
	}

	public void setRootGb(Long rootGb) {
		this.rootGb = rootGb;
	}

	public String getFlavorId() {
		return flavorId;
	}

	public void setFlavorId(String flavorId) {
		this.flavorId = flavorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getEphemeralGb() {
		return ephemeralGb;
	}

	public void setEphemeralGb(Long ephemeralGb) {
		this.ephemeralGb = ephemeralGb;
	}

	public Long getSwap() {
		return swap;
	}

	public void setSwap(Long swap) {
		this.swap = swap;
	}

	public static InstanceType fromMessage(Map<String, Object> message) {
		InstanceType type = new InstanceType();
		type.flavorId = (String) message.get("flavorid");
		type.name = (String) message.get("name");
		type.rootGb = toLong(message.get("root_gb"));
		type.ephemeralGb = toLong(message.get("ephemeral_gb"));
		type.memoryMb = toLong(message.get("memory_mb"));
		type.vcpus = toLong(message.get("vcpus"));
		type.swap = toLong(message.get("swap"));
		return type;
	}
}
