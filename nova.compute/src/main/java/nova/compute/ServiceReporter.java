/**
 * 
 */
package nova.compute;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import nova.compute.exception.RpcException;
import nova.compute.model.ComputeNode;
import nova.compute.model.ModelUtil;
import nova.compute.rpc.ConductorRpcAPI;
import nova.compute.rpc.ConnectionProxy;

/**
 * @author shida
 *
 */
public class ServiceReporter extends TimerTask {

	private static final String DEFAULT_BINARY = "nova-compute-windows";
	private static final String COMPUTE_TOPIC = "compute";
	final Logger logger = Logger.getLogger(ServiceReporter.class.getName());
	private ConductorRpcAPI api;
	private Host host;
	private Map<String, Object> service;
	private Context context;
	
	public ServiceReporter(ConnectionProxy proxy, ComputeConfig config, Context context) throws IOException {
		this.context = context;
		this.api = new ConductorRpcAPI(proxy);
		this.host = new Host(COMPUTE_TOPIC, DEFAULT_BINARY, config.getHost());
	}
	
	public Map<String, Object> init() throws IOException, RpcException {
		service = api.createService(context, host.toMap());
		ComputeNode node = new ComputeNode();
		node.setServiceId(ModelUtil.toInt(service.get("id")));
		api.createComputeNode(context, node);
		return service;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			logger.info("Pulse service activation report");
			host.setReportCount(host.getReportCount() + 1);
			api.updateService(context, service, host);
			logger.info("Reported. report count = " + host.getReportCount());
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to connect amqp", e);
		} catch (RpcException e) {
			logger.log(Level.WARNING, "Internal server error occured", e);
		}
	}

}
