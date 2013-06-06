/**
 * 
 */
package nova.compute.virt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nova.compute.ComputeConfig;
import nova.compute.model.ComputeNode;
import nova.compute.model.Instance;

import org.virtualbox_4_2.AccessMode;
import org.virtualbox_4_2.CleanupMode;
import org.virtualbox_4_2.DeviceType;
import org.virtualbox_4_2.IConsole;
import org.virtualbox_4_2.IHost;
import org.virtualbox_4_2.IMachine;
import org.virtualbox_4_2.IMedium;
import org.virtualbox_4_2.IProgress;
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
	 * 
	 * @param name
	 * @param imagePath
	 */
	public void spawn_with_image(Instance instance, String imagePath) {
		VirtualBoxManager manager = VirtualBoxManager.createInstance(null);
		manager.connect(config.getVboxUrl(), config.getVboxUser(),
				config.getVboxPassword());
		IVirtualBox vBox = manager.getVBox();
		ISession session = manager.getSessionObject();
		IMedium medium = vBox.openMedium(imagePath, DeviceType.HardDisk,
				AccessMode.ReadWrite, true);
		// TODO Control the medium to follow the instance type.
		IMachine machine = vBox.createMachine("", instance.getName(),
				new ArrayList<String>(), "Linux", "");
		machine.setExtraData("instance_uuid", instance.getUuid());
		machine.setName(instance.getHostName());
		machine.setDescription(instance.getDisplayDescription());
		machine.setMemorySize(instance.getMemoryMb());
		machine.setCPUCount(instance.getVcpus());
		machine.setVRAMSize(DEFAULT_VIDEO_MEMORY);
		IStorageController controller = machine.addStorageController("IDE",
				StorageBus.IDE);
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

	public void destroy(Instance instance) throws InterruptedException {
		VirtualBoxManager manager = VirtualBoxManager.createInstance(null);
		manager.connect(config.getVboxUrl(), config.getVboxUser(),
				config.getVboxPassword());
		IVirtualBox vBox = manager.getVBox();
		ISession session = manager.getSessionObject();
		List<IMachine> machines = vBox.getMachines();
		for (IMachine m : machines) {
			if (instance.getUuid().equals(m.getExtraData("instance_uuid"))) {
				m.lockMachine(session, LockType.Shared);
				IConsole console = session.getConsole();
				IProgress progress = console.powerDown();
				while (!progress.getCompleted()) {
					synchronized (this) {
						this.wait(1000);
					}
				}
				m.unregister(CleanupMode.Full);
			}
		}
	}
	
	public ComputeNode getComputeNode() {
		ComputeNode node = new ComputeNode();
		VirtualBoxManager manager = VirtualBoxManager.createInstance(null);
		manager.connect(config.getVboxUrl(), config.getVboxUser(),
				config.getVboxPassword());
		IVirtualBox vBox = manager.getVBox();
		IHost host = vBox.getHost();
		node.setFreeDiskGb(100);
		node.setFreeRamMb(host.getMemoryAvailable());
		node.setMemoryMb(host.getMemorySize());
		node.setMemoryMbUsed(host.getMemorySize() - host.getMemoryAvailable());
		node.setVcpus(host.getProcessorCount());
		return node;
	}
	
	public static boolean canConnect(ComputeConfig config) {
		try {
			VirtualBoxManager manager = VirtualBoxManager.createInstance(null);
			manager.connect(config.getVboxUrl(), config.getVboxUser(),
					config.getVboxPassword());
			manager.disconnect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
