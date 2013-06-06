/**
 * 
 */
package nova.compute.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import nova.compute.ComputeConfig;
import nova.compute.Context;
import nova.compute.exception.ConfigurationException;
import nova.compute.exception.ServiceException;
import nova.compute.model.Image;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Simple Glance Client to retrieve image to boot it.
 * Use OpenStack Java SDK's glance-client is more better.
 * @author shida
 *
 */
public class GlanceService {

	private ComputeConfig config;
	private Context context;

	public GlanceService(Context context, ComputeConfig config) {
		super();
		this.context = context;
		this.config = config;
	}
	
	public Image downloadImage(String imageId) throws ConfigurationException, ServiceException {
		try {
			HttpGet method = new HttpGet(getUrl("images", imageId));
			method.addHeader("X-Auth-Token", context.getAuthToken());
			method.addHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.getMimeType());
			HttpClientBuilder builder = HttpClientBuilder.create();
			HttpClient client = builder.build();
			HttpResponse response = client.execute(method);
			Image image = new Image();
			image.setId(imageId); // TODO should be retrieve from response.
			image.setBody(response.getEntity().getContent());
			return image;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	private URI getUrl(String action, String imageId) throws URISyntaxException {
		String uri = config.getGlanceEndpoint() + "/" + action + "/" + imageId;
		return new URI(uri);
	}

	@SuppressWarnings("unused")
	private URI getUrl(String action) throws URISyntaxException {
		String uri = config.getGlanceEndpoint() + "/" + action;
		return new URI(uri);
	}

}
