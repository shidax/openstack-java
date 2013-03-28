package nova.compute.rpc;

import java.io.IOException;

import nova.compute.ComputeConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DownloadTest {

	private ComputeConfig config;

	@Before
	public void setUp() throws Exception {
		config = new ComputeConfig();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDownload() throws IOException {
//		String token = "";
//		String endpoint = "";
//		String imageId = "";
//		GlanceClient client = new GlanceClient(endpoint,
//				token);
//		DownloadImage download = new DownloadImage(imageId);
//		ImageDownload downloadImage = client.execute(download);
//		DiskManager diskManager = new DiskManager(config);
//		diskManager.write(imageId, downloadImage.getInputStream());
	}

}
