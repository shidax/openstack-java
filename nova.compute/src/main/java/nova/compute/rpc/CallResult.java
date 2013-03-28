/**
 * 
 */
package nova.compute.rpc;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * @author shida
 *
 */
public class CallResult {

	private String failure;
	private Map<String, Object> result;
	@SerializedName("_msg_id")
	private String messageId;
	public String getFailure() {
		return failure;
	}
	public void setFailure(String failure) {
		this.failure = failure;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public Map<String, Object> getResult() {
		return result;
	}
	
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	
}
