/**
 * 
 */
package nova.compute;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nova.compute.rpc.ReplyMessage;


/**
 * @author shida
 *
 */
public class ComputeTask implements Runnable {

	private static Logger logger = Logger.getLogger(ComputeTask.class.getName());
	private boolean call;
	private Context context;
	private ComputeManager manager;
    private String method;
	private Map<String, Object> message;


	public ComputeTask(boolean call, Context context, ComputeManager manager,
			String method, Map<String, Object> message) {
		super();
		this.call = call;
		this.context = context;
		this.manager = manager;
		this.method = method;
		this.message = message;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			if (call) {
				logger.info("Start sync process " + method);
				ReplyMessage reply = new ReplyMessage();
				manager.execute(method, context, message, reply);
			} else {
				logger.info("Start async process " + method);
				manager.execute(method, context, message, null);
			}
		} catch (Throwable e) {
			logger.log(Level.WARNING, "Failed to process.", e);
			if (call) {
				// TODO send error reply
			}
		}
	}

}
