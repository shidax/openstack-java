/**
 * 
 */
package nova.compute.virt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nova.compute.ComputeConfig;
import nova.compute.model.Instance;

import org.virtualbox_4_2.AccessMode;
import org.virtualbox_4_2.DeviceType;
import org.virtualbox_4_2.IMachine;
import org.virtualbox_4_2.IMedium;
import org.virtualbox_4_2.ISession;
import org.virtualbox_4_2.IStorageController;
import org.virtualbox_4_2.IVirtualBox;
import org.virtualbox_4_2.LockType;
import org.virtualbox_4_2.StorageBus;
import org.virtualbox_4_2.VirtualBoxManager;

/**
 * @author shida
 * 
 */
public class VirtualMachineManager {

	private ComputeConfig config;

	private static final long DEFAULT_VIDEO_MEMORY = 32L;
	
	public VirtualMachineManager(ComputeConfig config) {
		this.config = config;
	}

	public void initInstanceDirectory() {
		File file = new File(config.getInstanceDirectory());
		if (!file.exists()) {
			file.mkdir();
		}
		File imageDir = new File(file.getPath() + "/base");
		if (!imageDir.exists()) {
			imageDir.mkdir();
		}
	}

	/**
	 * Temporary method for spawn instance.
	 * @param name
	 * @param imagePath
	 */
	public void spawn(String id, String name, Map<String, Object> instanceType, String imagePath) {
		VirtualBoxManager manager = VirtualBoxManager.createInstance(null);
		manager.connect(config.getVboxUrl(), config.getVboxUser(), config.getVboxPassword());
		IVirtualBox vBox = manager.getVBox();
		ISession session = manager.getSessionObject();
		IMedium medium = vBox.openMedium(imagePath, DeviceType.HardDisk, AccessMode.ReadWrite, true);
		IMachine machine = vBox.createMachine("", id, new ArrayList<String>(), "Linux", "");
		machine.setName(name);
		machine.setMemorySize(Double.valueOf(instanceType.get("memory_mb").toString()).longValue());
		machine.setVRAMSize(DEFAULT_VIDEO_MEMORY);
		IStorageController controller = machine.addStorageController("IDE", StorageBus.IDE);
		vBox.registerMachine(machine);
		machine.lockMachine(session, LockType.Shared);
		IMachine m = session.getMachine();
		m.attachDevice(controller.getName(), 0, 0, DeviceType.HardDisk, medium);
		m.saveSettings();
		session.unlockMachine();
		machine.launchVMProcess(session, "gui", "");		
	}
	
	public List<Instance> getAllInstances() {
		return new ArrayList<Instance>();
	}
}
