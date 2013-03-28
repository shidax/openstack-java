/**
 * 
 */
package nova.compute.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author shida
 *
 */
public class ComputeNode {
/**
 *     service_id = Column(Integer, ForeignKey('services.id'), nullable=True)
    service = relationship(Service,
                           backref=backref('compute_node'),
                           foreign_keys=service_id,
                           primaryjoin='and_('
                                'ComputeNode.service_id == Service.id,'
                                'ComputeNode.deleted == 0)')

    vcpus = Column(Integer)
    memory_mb = Column(Integer)
    local_gb = Column(Integer)
    vcpus_used = Column(Integer)
    memory_mb_used = Column(Integer)
    local_gb_used = Column(Integer)
    hypervisor_type = Column(Text)
    hypervisor_version = Column(Integer)
    hypervisor_hostname = Column(String(255))

    # Free Ram, amount of activity (resize, migration, boot, etc) and
    # the number of running VM's are a good starting point for what's
    # important when making scheduling decisions.
    free_ram_mb = Column(Integer)
    free_disk_gb = Column(Integer)
    current_workload = Column(Integer)
    running_vms = Column(Integer)

    # Note(masumotok): Expected Strings example:
    #
    # '{"arch":"x86_64",
    #   "model":"Nehalem",
    #   "topology":{"sockets":1, "threads":2, "cores":3},
    #   "features":["tdtscp", "xtpr"]}'
    #
    # Points are "json translatable" and it must have all dictionary keys
    # above, since it is copied from <cpu> tag of getCapabilities()
    # (See libvirt.virtConnection).
    cpu_info = Column(Text, nullable=True)
    disk_available_least = Column(Integer)
 */
	private String id;
	private int vcpus = 0;
	@SerializedName("memory_mb")
	private int memoryMb = 0;
	@SerializedName("local_gb")
	private int localGb = 0;
	@SerializedName("vcpus_used")
	private int vcpusUsed = 0;
	@SerializedName("memory_mb_used")
	private int memoryMbUsed = 0;
	@SerializedName("local_gb_used")
	private int localGbUsed = 0;
	@SerializedName("hypervisor_type")
	private String hypervisorType = "vbox";
	@SerializedName("hypervisor_version")
	private int hypervisorVersion = 1;
	@SerializedName("hypervisor_hostname")
	private String hypervisorHostName = "";
	@SerializedName("free_ram_mb")
	private int freeRamMb = 0;
	@SerializedName("free_disk_gb")
	private int freeDiskGb = 0;
	@SerializedName("current_workload")
	private int currentWorkload = 0;
	@SerializedName("running_vms")
	private int runningVms = 0;
	@SerializedName("cpu_info")
	private String cpuInfo = "";
	@SerializedName("disk_available_least")
	private int diskAvailableLeast;
	@SerializedName("service_id")
	private int serviceId;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public ComputeNode() {
		super();
	}
	
	public int getVcpus() {
		return vcpus;
	}
	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}
	public int getMemoryMb() {
		return memoryMb;
	}
	public void setMemoryMb(int memoryMb) {
		this.memoryMb = memoryMb;
	}
	public int getLocalGb() {
		return localGb;
	}
	public void setLocalGb(int localGb) {
		this.localGb = localGb;
	}
	public int getVcpusUsed() {
		return vcpusUsed;
	}
	public void setVcpusUsed(int vcpuUsed) {
		this.vcpusUsed = vcpuUsed;
	}
	public int getMemoryMbUsed() {
		return memoryMbUsed;
	}
	public void setMemoryMbUsed(int memoryMbUsed) {
		this.memoryMbUsed = memoryMbUsed;
	}
	public int getLocalGbUsed() {
		return localGbUsed;
	}
	public void setLocalGbUsed(int localGbUsed) {
		this.localGbUsed = localGbUsed;
	}
	public String getHypervisorType() {
		return hypervisorType;
	}
	public void setHypervisorType(String hypervisorType) {
		this.hypervisorType = hypervisorType;
	}
	public int getHypervisorVersion() {
		return hypervisorVersion;
	}
	public void setHypervisorVersion(int hypervisorVersion) {
		this.hypervisorVersion = hypervisorVersion;
	}
	public String getHypervisorHostName() {
		return hypervisorHostName;
	}
	public void setHypervisorHostName(String hypervisorHostName) {
		this.hypervisorHostName = hypervisorHostName;
	}
	public int getFreeRamMb() {
		return freeRamMb;
	}
	public void setFreeRamMb(int freeRamMb) {
		this.freeRamMb = freeRamMb;
	}
	public int getFreeDiskGb() {
		return freeDiskGb;
	}
	public void setFreeDiskGb(int freeDiskGb) {
		this.freeDiskGb = freeDiskGb;
	}
	public int getCurrentWorkload() {
		return currentWorkload;
	}
	public void setCurrentWorkload(int currentWorkload) {
		this.currentWorkload = currentWorkload;
	}
	public int getRunningVms() {
		return runningVms;
	}
	public void setRunningVms(int runningVms) {
		this.runningVms = runningVms;
	}
	public String getCpuInfo() {
		return cpuInfo;
	}
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}
	public int getDiskAvailableLeast() {
		return diskAvailableLeast;
	}
	public void setDiskAvailableLeast(int diskAvailableLeast) {
		this.diskAvailableLeast = diskAvailableLeast;
	}
	
}
