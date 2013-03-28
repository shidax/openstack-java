/**
 * 
 */
package nova.compute.model;

/**
 * @author shida
 *
 */
public class ModelUtil {

	public static int toInt(Object object) {
		if (object instanceof Double) {
			Double d = (Double) object;
			return d.intValue();
		}
		return 0;
	}
}
