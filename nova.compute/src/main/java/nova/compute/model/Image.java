/**
 * 
 */
package nova.compute.model;

import java.io.InputStream;

/**
 * @author shida
 * 
 */
public class Image {

	private String id;
	private InputStream body;

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

}
