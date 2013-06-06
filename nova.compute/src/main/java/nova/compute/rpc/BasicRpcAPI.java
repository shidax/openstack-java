package nova.compute.rpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import nova.compute.Context;
import nova.compute.exception.RpcException;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public abstract class BasicRpcAPI {

	final Logger logger = Logger.getLogger(BasicRpcAPI.class.getName());
	final String NOVA_TOPIC = "nova";

	private ConnectionProxy proxy;

	public BasicRpcAPI(ConnectionProxy proxy) throws IOException {
		this.proxy = proxy;
	}

	public abstract String getRoutingKey();

	@SuppressWarnings("unchecked")
	public CallResult call(Message message) throws IOException, RpcException {
		logger.info("Call to openstack api. msg_id is "
				+ message.getMessageId());
		String replyId = "reply_" + UUID.randomUUID().toString();
		this.declareReplyQueue(replyId);
		ReplyConsumer consumer = new ReplyConsumer(proxy.getReplyChannel(),
				replyId);
		message.setReply(replyId);
		this.send(message);
		try {
			Thread consumeThread = new Thread(consumer);
			consumeThread.start();
			consumeThread.join();
			Gson gson = new Gson();
			String resultString = consumer.getResult();
			logger.info("Receive call result: " + resultString);
			Map<String, Object> map = gson.fromJson(resultString, Map.class);
			CallResult result = gson.fromJson((String) map.get("oslo.message"), CallResult.class);
			if (result.getFailure() != null) {
				throw new RpcException(result.getFailure());
			}
			logger.info("Success to call");
			return result;
		} catch (RpcException e) {
			logger.log(Level.WARNING,
					"Failed to call for internal server error.", e);
			throw e;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to call for connection problem.",
					e);
			throw new RpcException(e);
		}
	}

	public void send(Message message) throws IOException {
		byte[] body = message.toJSON().getBytes();
		proxy.getChannel().basicPublish(
				NOVA_TOPIC,
				getRoutingKey(),
				false,
				new BasicProperties().builder().contentEncoding("utf-8")
						.contentType("application/json").build(), body);
	}

	protected Message createMessage(Context context, String method,
			Map<String, Object> body, String version) {
		String messageId = UUID.randomUUID().toString();
		Message message = new Message(method, messageId, context, version);
		Map<String, Object> args = new HashMap<String, Object>();
		args.putAll(body);
		message.setBody(args);
		return message;
	}

	/**
	 * Declare reply queue.
	 * 
	 * @param replyId
	 * @return reply queue id.
	 * @throws IOException
	 */
	private String declareReplyQueue(String replyId) throws IOException {
		proxy.getChannel().exchangeDeclare(replyId, "direct", false, true,
				new HashMap<String, Object>());
		proxy.getChannel().queueDeclare(replyId, false, false, true,
				new HashMap<String, Object>());
		proxy.getChannel().queueBind(replyId, replyId, replyId);
		return replyId;
	}

	class ReplyConsumer extends QueueingConsumer implements Runnable {

		private String queue;
		private byte[] results;

		public ReplyConsumer(Channel channel, String queue) {
			super(channel);
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				this.getChannel().basicConsume(this.queue, this);
				Delivery delivery = this.nextDelivery();
				synchronized (this) {
					this.results = delivery.getBody();
				}
				this.getChannel().queueDelete(queue);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ShutdownSignalException e) {
				e.printStackTrace();
			} catch (ConsumerCancelledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public String getResult() {
			synchronized (this) {
				if (results != null) {
					return new String(results);
				}
				return "";
			}
		}
	}

}