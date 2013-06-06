/**
 * 
 */
package nova.compute.rpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import nova.compute.ComputeConfig;
import nova.compute.ComputeManager;
import nova.compute.ComputeTask;
import nova.compute.Context;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author shida
 * 
 */
public class ComputeConsumer extends QueueingConsumer implements Runnable {

	final Logger logger = Logger.getLogger(ComputeConsumer.class.getName());

	private ConnectionProxy proxy;
	private ComputeConfig config;
	private ComputeManager manager;

	private ExecutorService executor;

	public ComputeConsumer(ConnectionProxy proxy, ComputeConfig config,
			ComputeManager manager) {
		super(proxy.getChannel());
		this.proxy = proxy;
		this.config = config;
		this.manager = manager;
		this.executor = Executors.newFixedThreadPool(10);
	}

	public void declareQueue() throws IOException {
		logger.info("Initialize nova/compute queues");
		// declare nova exchange.
		Channel channel = this.proxy.getChannel();
		channel.exchangeDeclare("nova", "topic");
		// declare nova-compute queue
		channel.queueDeclare("compute", false, false, false,
				new HashMap<String, Object>());
		// bind queue
		channel.queueBind("compute", "nova", "compute");
		// declare compute.host queue
		String queue = "compute." + config.getHost();
		channel.queueDeclare(queue, false, false, true,
				new HashMap<String, Object>());
		// bind queue
		channel.queueBind(queue, "nova", queue);
		// consume
		channel.basicConsume(queue, this);
		channel.basicConsume("compute", this);
		logger.info("Success to initialize queue. Start to comsume messages");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (true) {
			try {
				Delivery delivery = nextDelivery();
				logger.info("New message arrived. process it");
				String contents = new String(delivery.getBody());
				logger.info(contents);
				Gson gson = new Gson();
				Map<String, Object> message = gson.fromJson(contents, Map.class);
				Map<String, Object> osloMessage = gson.fromJson((String) message.get("oslo.message"), Map.class);
				Context context = Context.unpackContext(osloMessage);
				String method = (String) message.get("method");
				if (manager.hasMethod(method)) {
					ComputeTask task = new ComputeTask(false, context, manager, method, message);
					executor.execute(task);
				} else {
					logger.log(Level.WARNING, "Specified method " + method + " is not found.");
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "Failed to comsume message.", e);
				break;
			}
		}
	}

}
