/**
 * 
 */
package nova.compute.rpc;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import nova.compute.Context;

/**
 * AMQP Message Object. The message structure is following. { 'medhod' : method,
 * 'args' : args }
 * 
 * @author shida
 * 
 */
public class Message extends Context {

	private String method;
	@SerializedName("_msg_id")
	private String messageId;
	@SerializedName("args")
	private Object body;
	@SerializedName("version")
	private String version;
	@SerializedName("_reply_q")
	private String reply;

	/**
	 * Constructor.
	 * 
	 * @param method
	 * @param messageId
	 * @param context
	 * @param version
	 */
	public Message(String method, String messageId, Context context,
			String version) {
		super(context.getUserId(), context.getProjectId(), context.getRoles(),
				context.getReadDeleted(), context.getRemoteAddress(), context
						.getTimestamp(), context.getRequestId(), context
						.getAuthToken(), context.getServiceCatalog());
		setAdmin(context.isAdmin());
		setUseName(context.getUseName());
		setProjectName(context.getProjectName());
		this.method = method;
		this.messageId = messageId;
		this.version = version;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
