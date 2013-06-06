/**
 * 
 */
package nova.compute.ui;

import nova.compute.ComputeConfig;
import nova.compute.ComputeManager;
import nova.compute.ComputeService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

import com.woorea.openstack.base.client.OpenStackResponseException;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;

/**
 * @author shida
 * 
 */
public class ComputeServiceJob extends Job {

	public ComputeServiceJob() {
		super("ComputeServiceLauncher");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		ComputeConfig config = new ComputeConfig();
		config.setHost(store.getString("myhost"));
		config.setRabbitmqHost(store.getString("host"));
		config.setRabbitmqPassword(store.getString("password"));
		config.setRabbitmqUserName(store.getString("user"));
		config.setRabbitmqVirtualHost("/");
		config.setServiceUser(store.getString("osUser"));
		config.setServiceProject(store.getString("osProject"));
		config.setServicePassword(store.getString("osPassword"));
		config.setPeriod(10000L);
		config.setVboxUrl(store.getString("virtualBox"));
		config.setVboxUser(store.getString("winUser"));
		config.setVboxPassword(store.getString("winPass"));
		config.setGlanceEndpoint(store.getString("osGlance"));

		monitor.beginTask("Start OpenStack Compute", 10);
		monitor.subTask("Execute VBox WebService.");
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				boolean launch = MessageDialog
						.openQuestion(Display.getDefault().getActiveShell(),
								"Open VBoxWebServ?",
								"OpenStack Compute Java needs VBoxWebServ launched. Do you launch the service?");
				if (launch) {
					Program.launch(store.getString("vboxExec"));
				}
			}
		});
		monitor.worked(2);
		monitor.subTask("Check AMQP Connection");
		if (!ComputeService.canConnect(config)) {
			// Failed to connect.
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Failed to connect AMQP Service. Please check the URL and login Info");
		}
		monitor.worked(2);
		monitor.subTask("Check VBox Configuration.");
		if (!ComputeManager.checkDriverLive(config)) {
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Failed to connect Hypervisor Service. Please check the URL and login Info");
		}
		monitor.worked(2);
		monitor.subTask("Check OpenStack User Authentication.");
		Keystone keystone = new Keystone(store.getString("osUrl"));
		UsernamePassword auth = new UsernamePassword(config.getServiceUser(), config.getServicePassword());
		auth.setTenantName(config.getServiceProject());
		try {
			Access access = keystone.tokens().authenticate(auth).execute();
			Activator.getDefault().setAccess(access);
		} catch (OpenStackResponseException e) {
			e.printStackTrace();
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Failed to authenticate by Openstack Keystone. " +
					"Please check your Keystone url and authenticate infos.");			
		}
		ComputeService service = new ComputeService(config);
		Thread thread = new Thread(service);
		Activator.getDefault().setService(thread);
		thread.start();
		monitor.done();
		return Status.OK_STATUS;
	}

}
