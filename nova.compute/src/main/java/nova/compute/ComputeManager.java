/**
 * 
 */
package nova.compute;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nova.compute.exception.ConfigurationException;
import nova.compute.exception.RpcException;
import nova.compute.exception.ServiceException;
import nova.compute.model.Image;
import nova.compute.model.Instance;
import nova.compute.model.RequestSpec;
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

	static final Logger logger = Logger.getLogger(ComputeManager.class
			.getName());

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

	private void updateInstanceState(Context context, Instance instance)
			throws IOException, RpcException {
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("vm_state", instance.getVmState());
		updates.put("task_state", instance.getTaskState());
		updates.put("power_state", instance.getPowerState());
		api.updateInstance(context, instance.getUuid(), updates);
	}

	private void updateInstanceHost(Context context, Instance instance)
			throws IOException, RpcException {
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("host", instance.getHost());
		api.updateInstance(context, instance.getUuid(), updates);
	}

	@SuppressWarnings("unchecked")
	public void runInstance(Context context, Map<String, Object> message)
			throws IOException, ConfigurationException, ServiceException,
			RpcException {
		// TODO This is temporary code.
		// TODO Resolve glance endpoint from keystone.
		logger.info("Start to runInstance");
		Map<String, Object> args = (Map<String, Object>) message.get("args");
		RequestSpec spec = RequestSpec.fromMessage((Map<String, Object>) args
				.get("request_spec"));
		Instance instance = Instance.fromMessage((Map<String, Object>) args
				.get("instance"));
		Image image = spec.getImage();
		instance.setImage(image);
		logger.info("Change instance task_state to spawning");
		instance.setTaskState("spawning");
		instance.setHost(config.getHost());
		updateInstanceState(context, instance);
		updateInstanceHost(context, instance);
		if (!diskManager.isCached(image.getId())) {
			GlanceService service = new GlanceService(context, config);
			Image downloadImage = service.downloadImage(image.getId());
			diskManager.write(image.getId(), downloadImage.getBody());
		}
		String imagePath = diskManager.getImagePath(image.getId()).toString();
		String rootDisk = diskManager.createInstanceDirectory(imagePath,
				instance.getName());
		try {
			virtualMachineManager.spawn_with_image(instance, rootDisk);
			logger.info("Change instance vm_state to active");
			instance.setVmState("active");
			instance.setTaskState("none");
			updateInstanceState(context, instance);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to spawn instance.", e);
			instance.setVmState("error");
			instance.setTaskState(null);
			updateInstanceState(context, instance);
			diskManager.clearInstanceDirectory(instance.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public void terminateInstance(Context context, Map<String, Object> message) {
		Map<String, Object> args = (Map<String, Object>) message.get("args");
		Instance instance = Instance.fromMessage((Map<String, Object>) args
				.get("instance"));
		try {
			virtualMachineManager.destroy(instance);
			instance.setVmState("deleted");
			instance.setTaskState("none");
			updateInstanceState(context, instance);
			api.destroyInstance(context, instance);
		} catch (Exception e) {
			e.printStackTrace();
			instance.setVmState("error");
			try {
				updateInstanceState(context, instance);
			} catch (IOException | RpcException e1) {
				e1.printStackTrace();
			}
		}
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
	
	public static boolean checkDriverLive(ComputeConfig config) {
		return VirtualMachineManager.canConnect(config);
	}
}
