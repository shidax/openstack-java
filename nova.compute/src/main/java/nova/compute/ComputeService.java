/**
 * 
 */
package nova.compute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import nova.compute.exception.RpcException;
import nova.compute.rpc.ComputeConsumer;
import nova.compute.rpc.ConnectionProxy;

/**
 * An implements of nova-compute server.
 * 
 * @author shida
 * 
 */
public class ComputeService implements Runnable {

	static final Logger logger = Logger.getLogger(ComputeService.class
			.getName());
	private ComputeConfig config;
	private Timer timer;
	private Thread consumeThread;
	private Map<String, Object> service;
	private ComputeManager manager;
	private Context context;

	public ComputeService(ComputeConfig config) {
		this.config = config;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ComputeConfig config = new ComputeConfig();
		ComputeService service = new ComputeService(config);
		Thread main = new Thread(service);
		main.start();
		try {
			main.join();
		} catch (InterruptedException e) {
			logger.info("Interrupted to nova compute service");
		}
		logger.info("Stop to nova compute service");
		service.stop();
	}

	/**
	 * Stop this service.
	 */
	public void stop() {
		logger.info("Stop all services");
		if (consumeThread != null) {
			logger.info("Stop consumer thread");
			consumeThread.interrupt();
		}
		if (timer != null) {
			logger.info("Stop timer");
			timer.cancel();
		}
		if (service != null) {
			try {
				logger.info("Destroy service.");
				manager.getApi().destroyService(context, service);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RpcException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			logger.info("Start to nova compute service");
			context = getServiceContext(config);
			ConnectionProxy proxy = connect(config);
			manager = new ComputeManager(proxy, config);
			ComputeConsumer consumer = new ComputeConsumer(proxy, config, manager);
			consumer.declareQueue();
			ServiceReporter reporter = new ServiceReporter(proxy, config, context);
			service = reporter.init();
			timer = new Timer();
			timer.schedule(reporter, config.getPeriod(), config.getPeriod());
			consumeThread = new Thread(consumer);
			consumeThread.start();
			consumeThread.join();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to boot service", e);
		} catch (RpcException e) {
			logger.log(Level.WARNING, "Some error occured in server", e);
		} catch (InterruptedException e) {
			logger.info("Interrupted to nova compute service");
		}
	}

	private ConnectionProxy connect(ComputeConfig config) throws IOException {
		ConnectionProxy proxy = new ConnectionProxy(config.getRabbitmqHost(),
				config.getRabbitmqVirtualHost(), config.getRabbitmqUserName(),
				config.getRabbitmqPassword());
		proxy.connect();
		return proxy;
	}

	/**
	 * Create a context for used by service.
	 * 
	 * @param config
	 * @return
	 */
	private Context getServiceContext(ComputeConfig config) {
		// Fill in fake value.
		Context context = new Context(config.getServiceUser(),
				config.getServiceProject(), new ArrayList<String>(), "no",
				config.getHost(), new Date().toString(), "-", "-",
				new ArrayList<ServiceCatalog>());
		context.setAdmin(true);
		context.setProjectName(config.getServiceProject());
		context.setUseName(config.getServiceUser());
		return context;
	}
}
