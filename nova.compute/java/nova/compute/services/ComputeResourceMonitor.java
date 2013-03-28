/**
 * 
 */
package nova.compute.services;

import java.util.TimerTask;

import nova.compute.ComputeConfig;

/**
 * @author shida
 *
 */
public class ComputeResourceMonitor extends TimerTask {

	private ComputeConfig config;
	
	public ComputeResourceMonitor(ComputeConfig config) {
		super();
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		// Oops, Java can't monitor none jvm info.
		
	}

}
