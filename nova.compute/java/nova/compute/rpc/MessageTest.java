package nova.compute.rpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;
import nova.compute.Context;
import nova.compute.ServiceCatalog;
import com.google.gson.Gson;

public class MessageTest extends TestCase {

	public void testMessage() {
		Context context = new Context("fake_uid", "fake_pid",
				new ArrayList<String>(), "no", "fake_addr",
				new Date().toString(), UUID.randomUUID().toString(),
				"fake_token", new ArrayList<ServiceCatalog>());
		Message message = new Message("ping", UUID.randomUUID().toString(), context, "");
		Gson gson = new Gson();
		String jsonString = gson.toJson(message);
		Message parsed = gson.fromJson(jsonString, Message.class);
		assertEquals(message.getMessageId(), parsed.getMessageId());
	}

}
