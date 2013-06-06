package nova.compute.ui.views;

import java.util.UUID;

import nova.compute.ui.Activator;
import nova.compute.ui.ComputeServiceJob;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class ConfigView extends ViewPart {

	private final class ExecuteAction extends Action {
		public void run() {
			Job job = new ComputeServiceJob();
			job.setUser(true);
			job.schedule();
		}
	}

	/**
	 * Simple save action.
	 * @author shida
	 *
	 */
	private final class SaveAction extends Action {
		public void run() {
			Job job = new Job("Save Configuration") {
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							IPreferenceStore store = Activator.getDefault().getPreferenceStore();
							store.setValue("myhost", myHostText.getText());
							store.setValue("host", hostText.getText());
							store.setValue("port", portText.getText());
							store.setValue("user", userText.getText());
							store.setValue("password", passwordText.getText());
							store.setValue("osUrl", osUrlText.getText());
							store.setValue("osUser", osUserText.getText());
							store.setValue("osProject", osProjectText.getText());
							store.setValue("osPassword", osPasswordText.getText());
							store.setValue("osGlance", osGlanceText.getText());
							store.setValue("vboxexec", vboxExecText.getText());
							store.setValue("virtualBox", virtualBoxText.getText());
							store.setValue("winUser", winUserText.getText());
							store.setValue("winPass", winPassText.getText());
							try {
								InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).flush();
							} catch (BackingStoreException e) {
								e.printStackTrace();
							}
						}
					});
					return Status.OK_STATUS;
				}
			};
			job.setUser(true);
			job.schedule();
		}
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "nova.compute.ui.views.ConfigView";

	private Action saveAction;
	private Action executeAction;

	private Text hostText;

	private Text portText;

	private Text userText;

	private Text passwordText;

	private Text osUserText;

	private Text osProjectText;

	private Text osPasswordText;

	private Text osGlanceText;

	private Text virtualBoxText;

	private Text winUserText;

	private Text winPassText;

	private Text myHostText;

	private Text vboxExecText;

	private Button vboxButton;

	private Text osUrlText;

	/**
	 * The constructor.
	 */
	public ConfigView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		Composite root = new Composite(parent, SWT.NONE);
		root.setLayoutData(new GridData(GridData.FILL_BOTH));
		root.setLayout(new GridLayout(1, false));
		Label description = new Label(root, SWT.NONE);
		description.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		description
				.setText("Following the configuration for OpenStack Nova Compute Java.");
		Group basic = new Group(root, SWT.NONE);
		basic.setLayout(new GridLayout(2, false));
		basic.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		basic.setText("Basic Configuration");
		Label myHostLabel = new Label(basic, SWT.NONE);
		myHostLabel.setText("Compute Host:");
		myHostLabel
				.setToolTipText("Setting the host name.");
		myHostText = new Text(basic, SWT.BORDER);
		myHostText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label hostLabel = new Label(basic, SWT.NONE);
		hostLabel.setText("AMQP server host &name:");
		hostLabel
				.setToolTipText("Setting the host name or ip for connected to AMQP server.");
		hostText = new Text(basic, SWT.BORDER);
		hostText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label portLabel = new Label(basic, SWT.NONE);
		portLabel.setText("AMQP server &port:");
		portLabel
				.setToolTipText("Setting the port number for connected to AMQP server.");
		portText = new Text(basic, SWT.BORDER);
		portText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label userLabel = new Label(basic, SWT.NONE);
		userLabel.setText("AMQP server login &user:");
		userLabel
				.setToolTipText("Setting the login user for connected to AMQP server.");
		userText = new Text(basic, SWT.BORDER);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label passwordLabel = new Label(basic, SWT.NONE);
		passwordLabel.setText("AMQP server pass&word:");
		passwordLabel
				.setToolTipText("Setting the login password for connected to AMQP server.");
		passwordText = new Text(basic, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Group auth = new Group(root, SWT.NONE);
		auth.setLayout(new GridLayout(2, false));
		auth.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		auth.setText("Authenticate Configuration");
		Label osUrlLabel = new Label(auth, SWT.NONE);
		osUrlLabel.setText("Keystone &Endpoint:");
		osUrlLabel
				.setToolTipText("Setting the endpoint for openstack.");
		osUrlText = new Text(auth, SWT.BORDER);
		osUrlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label osUserLabel = new Label(auth, SWT.NONE);
		osUserLabel.setText("OpenStack &User:");
		osUserLabel
				.setToolTipText("Setting the user id for login to openstack.");
		osUserText = new Text(auth, SWT.BORDER);
		osUserText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label osProjectLabel = new Label(auth, SWT.NONE);
		osProjectLabel.setText("OpenStack &Project:");
		osProjectLabel
				.setToolTipText("Setting the project/tenant for login to openstack.");
		osProjectText = new Text(auth, SWT.BORDER);
		osProjectText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label osPasswordLabel = new Label(auth, SWT.NONE);
		osPasswordLabel.setText("OpenStack &Password:");
		osPasswordLabel
				.setToolTipText("Setting the password for login to openstack.");
		osPasswordText = new Text(auth, SWT.BORDER);
		osPasswordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label glanceLabel = new Label(auth, SWT.NONE);
		glanceLabel.setText("OpenStack &Glance endpoint:");
		glanceLabel.setToolTipText("Setting the url for openstack glance.");
		osGlanceText = new Text(auth, SWT.BORDER);
		osGlanceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Group vbox = new Group(root, SWT.NONE);
		vbox.setLayout(new GridLayout(2, false));
		vbox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		vbox.setText("Hypervisor Configuration");
		Label vboxExecLabel = new Label(vbox, SWT.NONE);
		vboxExecLabel.setText("VirutalBox WebService Exec Path:");
		vboxExecLabel.setToolTipText("Setting the full path for virtualBox execution file.");
		Composite composite = new Composite(vbox, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		vboxExecText = new Text(composite, SWT.BORDER);
		vboxExecText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		vboxButton = new Button(composite, SWT.NONE);
		vboxButton.setText("...");
		
		Label vboxUrlLabel = new Label(vbox, SWT.NONE);
		vboxUrlLabel.setText("VirutalBox URL:");
		vboxUrlLabel.setToolTipText("Setting the url for openstack glance.");
		virtualBoxText = new Text(vbox, SWT.BORDER);
		virtualBoxText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label winUserLabel = new Label(vbox, SWT.NONE);
		winUserLabel.setText("Windows Login &User:");
		winUserLabel.setToolTipText("Setting the user for windows.");
		winUserText = new Text(vbox, SWT.BORDER);
		winUserText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label winPassLabel = new Label(vbox, SWT.NONE);
		winPassLabel.setText("Windows Login &Password:");
		winPassLabel.setToolTipText("Setting the user for windows.");
		winPassText = new Text(vbox, SWT.BORDER | SWT.PASSWORD);
		winPassText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		initData();
		makeActions();
		contributeToActionBars();
	}

	private void initData() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault("myhost", UUID.randomUUID().toString());
		store.setDefault("host", "localhost");
		store.setDefault("port", "5672");
		store.setDefault("user", "guest");
		store.setDefault("password", "");
		store.setDefault("osUrl", "http://localhost:5000/");
		store.setDefault("osUser", "nova-windows");
		store.setDefault("osProject", "nova-windows");
		store.setDefault("osPassword", "");
		store.setDefault("osGlance", "http://192.168.153.253:9292/v1");
		store.setDefault("vboxExec", "C:/Program Files/Oracle/VirtualBox/VBoxWebSrv.exe");
		store.setDefault("virtualBox", "http://localhost:18083");
		store.setDefault("winUser", "user");
		store.setDefault("winPass", "");
		
		myHostText.setText(store.getString("myhost"));
		hostText.setText(store.getString("host"));
		portText.setText(store.getString("port"));
		userText.setText(store.getString("user"));
		passwordText.setText(store.getString("password"));
		osUrlText.setText(store.getString("osUrl"));
		osUserText.setText(store.getString("osUser"));
		osProjectText.setText(store.getString("osProject"));
		osPasswordText.setText(store.getString("osPassword"));
		osGlanceText.setText(store.getString("osGlance"));
		vboxExecText.setText(store.getString("vboxExec"));
		virtualBoxText.setText(store.getString("virtualBox"));
		winUserText.setText(store.getString("winUser"));
		winPassText.setText(store.getString("winPass"));
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(saveAction);
		manager.add(new Separator());
		manager.add(executeAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(saveAction);
		manager.add(executeAction);
	}

	private void makeActions() {
		saveAction = new SaveAction();
		saveAction.setText("Save Configuration");
		saveAction.setToolTipText("Save these configurations.");
		saveAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));

		executeAction = new ExecuteAction();
		executeAction.setText("Execute Compute");
		executeAction.setToolTipText("Execute OpenStack Compute for this configuration.");
		executeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}
}