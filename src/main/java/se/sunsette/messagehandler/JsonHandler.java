package se.sunsette.messagehandler;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import se.sunsette.model.Message;

public class JsonHandler implements Decoder.Text<Message> {

	@Override
	public void destroy() {

	}

	@Override
	public void init(EndpointConfig arg0) {

	}

	@Override
	public Message decode(String message) throws DecodeException {
		System.out.println(message);
		JsonReader jr = Json.createReader(new StringReader(message));
		JsonObject jo = jr.readObject();
		jr.close();

		String currentMethod = jo.get("method").toString();
		String newMethod = currentMethod.replace("\"", "");

		Message currentMessage = new Message(newMethod, jo);

		return currentMessage;
	}

	@Override
	public boolean willDecode(String arg0) {
		return true;
	}

}
