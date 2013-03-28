/**
 * 
 */
package nova.compute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import nova.compute.exception.ConfigurationException;
import nova.compute.exception.ServiceException;
import nova.compute.model.Image;
import nova.compute.rpc.ConductorRpcAPI;
import nova.compute.rpc.ConnectionProxy;
import nova.compute.rpc.ReplyMessage;
import nova.compute.services.GlanceService;
import nova.compute.virt.DiskManager;
import nova.compute.virt.VirtualMachineManager;

/**
 * @author shida
 * 
 */
public class ComputeManager {

	private static Map<String, Method> METHODS = new HashMap<String, Method>();
	static {
		try {
			METHODS.put("run_instance", ComputeManager.class.getMethod(
					"runInstance", Context.class, Map.class));
			METHODS.put("terminate_instance", ComputeManager.class.getMethod(
					"terminateInstance", Context.class, Map.class));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	private ConductorRpcAPI api;
	private ComputeConfig config;
	private VirtualMachineManager virtualMachineManager;
	private DiskManager diskManager;

	public ComputeManager(ConnectionProxy proxy, ComputeConfig config)
			throws IOException {
		this.config = config;
		api = new ConductorRpcAPI(proxy);
		virtualMachineManager = new VirtualMachineManager(config);
		diskManager = new DiskManager(config);
		this.init();
	}

	private void init() {
		virtualMachineManager.initInstanceDirectory();
	}

	public ConductorRpcAPI getApi() {
		return api;
	}

	public void setApi(ConductorRpcAPI api) {
		this.api = api;
	}

	public void runInstance(Context context, Map<String, Object> message)
			throws IOException, ConfigurationException, ServiceException {
		// TODO This is temporary code.
		// TODO Resolve glance endpoint from keystone.
		Map<String, Object> args = (Map<String, Object>) message.get("args");
		Map<String, Object> spec = (Map<String, Object>) args.get("request_spec");
		Map<String, Object> image = (Map<String, Object>) spec.get("image");
		Map<String, Object> instance = (Map<String, Object>) args.get("instance");
		Map<String, Object> instanceType = (Map<String, Object>) spec.get("instance_type");
		String imageId = (String) image.get("id");
		String instanceId = (String) instance.get("uuid");
		String name = (String) instance.get("hostname");
		if (!diskManager.isCached(imageId)) {
			GlanceService service = new GlanceService(context, config);
			Image downloadImage = service.downloadImage(imageId);
			diskManager.write(imageId,
					downloadImage.getBody());
		}
		virtualMachineManager.spawn(instanceId, name, instanceType,
				diskManager.getImagePath(imageId).toString());
	}

	public void terminateInstance(Context context, Map<String, Object> message) {

	}

	public boolean hasMethod(String method) {
		return METHODS.containsKey(method);
	}

	public void execute(String method, Context context,
			Map<String, Object> message, ReplyMessage reply) throws Throwable {
		Method m = METHODS.get(method);
		try {
			if (reply == null) {
				m.invoke(this, context, message);
			} else {
				m.invoke(this, context, message, reply);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw e.getTargetException();
		}
	}
}
