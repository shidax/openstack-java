/**
 * 
 */
package nova.compute.rpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nova.compute.Context;
import nova.compute.Host;
import nova.compute.exception.RpcException;
import nova.compute.model.ComputeNode;
import nova.compute.model.Instance;

/**
 * @author shida
 * 
 */
public class ConductorRpcAPI extends BasicRpcAPI {

	final String CONDUCTOR_TOPIC = "conductor";

	public ConductorRpcAPI(ConnectionProxy proxy) throws IOException {
		super(proxy);
	}

	public Map<String, Object> ping(Context context) throws IOException, RpcException {
		String argp = "1.21 GigaWatts";
		String version = "1.22";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("arg", argp);
		Message message = this.createMessage(context, "ping", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public Map<String, Object> getInstance(Context context, String instanceId) throws IOException, RpcException {
		String version = "1.24";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("instance_id", instanceId);
		Message message = this.createMessage(context, "instance_get", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public Map<String, Object> updateInstance(Context context, String uuid, Map<String, Object> updates) throws IOException, RpcException {
		String version = "1.38";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("instance_uuid", uuid);
		body.put("updates", updates);
		Message message = this.createMessage(context, "instance_update", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public Map<String, Object> destroyInstance(Context context, Instance instance) throws IOException, RpcException {
		String version = "1.16";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("instance", instance);
		Message message = this.createMessage(context, "instance_destroy", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}
	
	public Map<String, Object> getInstanceByUUID(Context context, String uuid) throws IOException, RpcException {
		String version = "1.24";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("instance_uuid", uuid);
		Message message = this.createMessage(context, "instance_get_by_uuid", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public Map<String, Object> getInstanceAll(Context context) throws IOException, RpcException {
		String version = "1.23";
		Map<String, Object> body = new HashMap<String, Object>();
		Message message = this.createMessage(context, "instance_get_all", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public Map<String, Object> createService(Context context, Map<String, Object> host) throws IOException, RpcException {
		String version = "1.27";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("values", host);
		Message message = this.createMessage(context, "service_create", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public Map<String, Object> updateService(Context context, Map<String, Object> service, Host host) throws IOException, RpcException {
		String version = "1.34";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("service", service);
		body.put("values", host.toMap());
		Message message = this.createMessage(context, "service_update", body, version);
		CallResult result = this.call(message);
		return result.getResult();
	}

	public void destroyService(Context context, Map<String, Object> service) throws IOException, RpcException {
		String version = "1.29";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("service_id", service.get("id"));
		Message message = this.createMessage(context, "service_destroy", body, version);
        this.call(message);
	}

	public Map<String, Object> createComputeNode(Context context, ComputeNode values) throws IOException, RpcException {
		String version = "1.33";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("values", values);
		Message message = this.createMessage(context, "compute_node_create", body, version);
        CallResult call = this.call(message);
        return call.getResult();
	}

	public Map<String, Object> updateComputeNode(Context context, ComputeNode values) throws IOException, RpcException {
		String version = "1.33";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("node", values);
		body.put("values", values);
		Message message = this.createMessage(context, "compute_node_update", body, version);
        CallResult call = this.call(message);
        return call.getResult();
	}

	public void destroyComputeNode(Context context, ComputeNode node) throws IOException, RpcException {
		String version = "1.44";
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("node", node);
		Message message = this.createMessage(context, "compute_node_delete", body, version);
        this.call(message);
	}

	@Override
	public String getRoutingKey() {
		return CONDUCTOR_TOPIC;
	}

}
