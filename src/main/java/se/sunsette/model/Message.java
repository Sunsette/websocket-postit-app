package se.sunsette.model;

import javax.json.JsonObject;

public class Message {

	private String method;
	private JsonObject data;

	public Message(String type, JsonObject data) {

		this.method = type;
		this.data = data;
	}

	public String getMethod() {
		return method;
	}

	public JsonObject getData() {
		return data;
	}

}
