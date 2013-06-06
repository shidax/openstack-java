package nova.compute.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.addView("nova.compute.ui.views.MainMenuView", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
		IFolderLayout folder = layout.createFolder("nova.compute.ui.main", IPageLayout.LEFT, 0.8f, IPageLayout.ID_EDITOR_AREA);
		folder.addView("nova.compute.ui.views.ConfigView");
//		folder.addView("nova.compute.ui.views.DashboadView");
//		folder.addView("nova.compute.ui.views.TaskView");
//		folder.addView("nova.compute.ui.views.ImageView");
//		folder.addView("nova.compute.ui.views.VolumeView");
//		folder.addView("nova.compute.ui.views.NetworkView");
		layout.setEditorAreaVisible(false);
	}
}
