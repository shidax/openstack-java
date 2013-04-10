/**
 * 
 */
package nova.compute.model;

import java.util.Map;

/**
 * @author shida
 * 
 */
public class RequestSpec {

	private Image image;
	private InstanceType instanceType;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public InstanceType getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(InstanceType instanceType) {
		this.instanceType = instanceType;
	}

	@SuppressWarnings("unchecked")
	public static RequestSpec fromMessage(Map<String, Object> value) {
		RequestSpec spec = new RequestSpec();
		Map<String, Object> image = (Map<String, Object>) value.get("image");
		spec.image = Image.fromMessage(image);
		Map<String, Object> instanceType = (Map<String, Object>) value
				.get("instance_type");
		spec.instanceType = InstanceType.fromMessage(instanceType);
		return spec;
	}
}
