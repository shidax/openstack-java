/**
 * 
 */
package nova.compute.rpc;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author shida
 *
 */
public class ConnectionProxy {

	private Channel channel;
	private ConnectionFactory factory;
	private Connection connection;

	public ConnectionProxy(String host, String virtualHost, String username,
			String password) {
		super();
		factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setVirtualHost(virtualHost);
		factory.setUsername(username);
		factory.setPassword(password);
	}
	
	public Channel connect() throws IOException {
		this.connection = factory.newConnection();
		this.channel = connection.createChannel();
		return channel;
	}
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public Channel getReplyChannel() throws IOException {
		return factory.newConnection().createChannel();
	}
	
	public void close() throws IOException {
		this.connection.close();
	}
}
