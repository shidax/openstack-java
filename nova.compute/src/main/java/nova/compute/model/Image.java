/**
 * 
 */
package nova.compute.model;

import java.io.InputStream;
import java.util.Map;
import static nova.compute.model.ModelUtil.toLong;
/**
 * @author shida
 * 
 */
public class Image {

	private String id;
	private InputStream body;
	private String status;
	private String name;
	private String containerFormat;
	private String diskFormat;
	private String owner;
	private Boolean isPublic;
	private Long size;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InputStream getBody() {
		return body;
	}

	public void setBody(InputStream body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContainerFormat() {
		return containerFormat;
	}

	public void setContainerFormat(String containerFormat) {
		this.containerFormat = containerFormat;
	}

	public String getDiskFormat() {
		return diskFormat;
	}

	public void setDiskFormat(String diskFormat) {
		this.diskFormat = diskFormat;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public static Image fromMessage(Map<String, Object> message) {
		Image image = new Image();
		image.status = (String) message.get("status");
		image.id = (String) message.get("id");
		image.name = (String) message.get("name");
		image.diskFormat = (String) message.get("disk_format");
		image.containerFormat = (String) message.get("container_format");
		image.isPublic = (Boolean) message.get("is_public");
		image.owner = (String) message.get("owner");
		image.size = toLong(message.get("size"));
		return image;
	}
}
