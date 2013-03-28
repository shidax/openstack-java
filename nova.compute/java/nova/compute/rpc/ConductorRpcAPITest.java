package nova.compute.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.TestCase;
import nova.compute.Context;
import nova.compute.Host;
import nova.compute.ServiceCatalog;
import nova.compute.exception.RpcException;
import nova.compute.model.ComputeNode;
import nova.compute.model.ModelUtil;

public class ConductorRpcAPITest extends TestCase {

	public ConnectionProxy createProxy() throws IOException {
		ConnectionProxy proxy = new ConnectionProxy("192.168.153.248", "/", "guest", "rabbit");
		proxy.connect();
		return proxy;
	}
	
	public void testSend() throws IOException {
		ConnectionProxy proxy = this.createProxy();
		BasicRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Message message = new Message("test", UUID.randomUUID().toString(), context, "1.0.0");
		api.send(message);
	}

	public void testPing() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Map<String, Object> result = api.ping(context);
		System.out.println(result);
		assertEquals("conductor", result.get("service"));
		assertEquals("1.21 GigaWatts", result.get("arg"));
	}

	public void testGetInstance() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Map<String, Object> result;
		try {
			// Instance of fake id are not exist.
			result = api.getInstance(context, "fakeId");
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetInstanceByUUID() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Map<String, Object> result;
		try {
			// Instance of fake id are not exist.
			result = api.getInstanceByUUID(context, "fakeId");
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetInstanceAll() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Map<String, Object> result;
		result = api.getInstanceAll(context);
		assertEquals(new HashMap<String, Object>(), result);
	}

	public void testCreateService() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Host host = new Host("compute", "nova-windows", "test_host");
		Map<String, Object> result = api.createService(context, host.toMap());
		assertEquals(result.get("binary"), "nova-windows");
		assertEquals(result.get("host"), "test_host");
	}

	public void testUpdateService() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Host host = new Host("compute", "nova-windows", "test_host");
		Map<String, Object> service = api.createService(context, host.toMap());
		double reportCount = (Double) service.get("report_count");
		assertEquals(0.0, reportCount);
		host.setReportCount(1);
		Map<String, Object> updateService = api.updateService(context, service, host);
		assertEquals(1.0, updateService.get("report_count"));
	}

	public void testDestroyService() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Host host = new Host("compute", "nova-windows", "test_host");
		Map<String, Object> result = api.createService(context, host.toMap());
		assertEquals(result.get("binary"), "nova-windows");
		assertEquals(result.get("host"), "test_host");
		api.destroyService(context, result);
	}

	public void testCreateDestroyComputeNode() throws IOException, RpcException {
		ConnectionProxy proxy = this.createProxy();
		ConductorRpcAPI api = new ConductorRpcAPI(proxy);
		Context context = createFakeContext();
		Host host = new Host("compute", "nova-windows", "test_host");
		Map<String, Object> result = api.createService(context, host.toMap());
		ComputeNode node = new ComputeNode();
		System.out.println(result);
		node.setServiceId(ModelUtil.toInt(result.get("id")));
		Map<String, Object> nodeResult = api.createComputeNode(context, node);
		api.destroyService(context, result);
		assertEquals(result.get("id"), nodeResult.get("service_id"));
	}

	private Context createFakeContext() {
		Context context = new Context("fake_uid", "fake_pid",
				new ArrayList<String>(), "no", "fake_addr",
				new Date().toString(), UUID.randomUUID().toString(),
				"fake_token", new ArrayList<ServiceCatalog>());
		context.setAdmin(true);
		context.setProjectName("hoge");
		context.setUseName("hoge");
		return context;
	}

	
}
