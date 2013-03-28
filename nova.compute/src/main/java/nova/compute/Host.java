/**
 * 
 */
package nova.compute;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shida
 *
 */
public class Host {

	private String topic;
	private String binary;
	private String host;
	private int reportCount = 0;

	public Host(String topic, String binary, String host) {
		super();
		this.topic = topic;
		this.binary = binary;
		this.host = host;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getBinary() {
		return binary;
	}

	public void setBinary(String binary) {
		this.binary = binary;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getReportCount() {
		return reportCount;
	}

	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("host", host);
		map.put("binary", binary);
		map.put("topic", topic);
		map.put("report_count", reportCount);
		return map;
	}
}
