package nova.compute.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private TrayItem trayItem;

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(400, 300));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
	}

	@Override
	public boolean preWindowShellClose() {
//		Image icon = Activator.getImageDescriptor("icons/openstack16.png").createImage();
//		trayItem = new TrayItem(Display.getDefault().getSystemTray(), SWT.NONE);
//		trayItem.setText("OpenStack Nova");
//		trayItem.setImage(icon);
//		trayItem.setToolTipText("Configuration tool for OpenStack Nova Compute on Windows");
//		trayItem.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				super.widgetSelected(e);
//				Shell workbenchWindowShell = getWindowConfigurer().getWindow()
//						.getShell();
//				workbenchWindowShell.setVisible(true);
//				workbenchWindowShell.setActive();
//				workbenchWindowShell.setFocus();
//				workbenchWindowShell.setMinimized(false);
//				trayItem.dispose();
//			}
//		});
////		trayItem.
//		getWindowConfigurer().getWindow().getShell().setVisible(false);
//		return false;
		return true;
	}
}
