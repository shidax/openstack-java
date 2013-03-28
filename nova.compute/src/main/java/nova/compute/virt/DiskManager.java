/**
 * 
 */
package nova.compute.virt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nova.compute.ComputeConfig;

/**
 * @author shida
 * 
 */
public class DiskManager {

	private static final String IMAGE_PATH = "base";
	private static final int CHUNK = 1024;
	
	private ComputeConfig config;
	
	public DiskManager(ComputeConfig config) {
		this.config = config;
	}

	/**
	 * Return if specified image is exists or not.
	 * @param imageName
	 * @return
	 */
	public boolean isCached(String imageName) {
		File out = new File(config.getInstanceDirectory() + "/" + IMAGE_PATH + "/" + imageName);
		return out.exists();
	}
	
	public String getImagePath(String imageName) {
		File out = new File(config.getInstanceDirectory() + "/" + IMAGE_PATH + "/" + imageName);
		return out.getAbsolutePath();
	}
	/**
	 * Write the image to local disk. 
	 * @param imageName imageName
	 * @param stream input stream for the image
	 * @return
	 * @throws IOException 
	 */
	public String write(String imageName, InputStream stream) throws IOException {
		File out = new File(config.getInstanceDirectory() + "/" + IMAGE_PATH + "/" + imageName);
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(out));
		BufferedInputStream inputStream = new BufferedInputStream(stream);
		try {
//			byte[] buffer = new byte[CHUNK];
			int b;
			while ((b = inputStream.read()) >= 0) {
				outputStream.write(b);
			}
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			outputStream.close();
		}
		return out.getAbsolutePath();
	}
}
