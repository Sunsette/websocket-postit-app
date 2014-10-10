package se.sunsette.websockets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import se.sunsette.messagehandler.JsonHandler;
import se.sunsette.model.Message;
import se.sunsette.model.Postit;
import se.sunsette.model.Whiteboard;
import se.sunsette.repository.*;

@ServerEndpoint(value = "/shout", decoders = { JsonHandler.class })
public class MyWebSocketEndPoint {

	private static Set<Session> clients = Collections
			.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void myOnOpen(Session session) {
		System.out.println("Websocket opened: " + session);
		clients.add(session);
	}

	@OnMessage
	public void myOnMessage(Session session, Message message)
			throws IOException {

		String method = message.getMethod();
		JsonObject data = message.getData();

		PostitRepository pr = new PostitRepository();
		WhiteboardRepository wr = new WhiteboardRepository();

		if ("add".equals(method)) {

			addPostit(data, pr);

		} else if ("delete".equals(method)) {

			deletePostit(data, pr);

		} else if ("update".equals(method)) {

			updatePostit(data, pr);

		} else if ("getWhiteboards".equals(method)) {

			getAllWhiteboards(session, wr);

		} else if ("getWhiteboardPostits".equals(method)) {

			getWhiteboardPostits(session, data, pr);

		} else if ("addWhiteboard".equals(method)) {

			addWhiteboard(data, wr);

		} else if ("deleteWhiteboard".equals(method)) {

			deleteWhiteboard(data, wr);

		} else {
			System.out.println("Didn't work");
		}
	}

	private void deleteWhiteboard(JsonObject data, WhiteboardRepository wr)
			throws IOException {
		int whiteboardId = Integer.parseInt(data.get("id").toString()
				.replace("\"", ""));

		wr.deleteWhiteboard(whiteboardId);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		jab.add(Json.createObjectBuilder().add("method", "deleteWhiteboard")
				.build());

		data = Json.createObjectBuilder().add("id", whiteboardId).build();

		jab.add(data);

		JsonArray ja = jab.build();

		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText(ja.toString());

			}
		}
	}

	private void addWhiteboard(JsonObject data, WhiteboardRepository wr)
			throws IOException {
		String whiteboardName = data.get("name").toString().replace("\"", "");

		int whiteboardId = wr.addWhiteboard(whiteboardName);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		jab.add(Json.createObjectBuilder().add("method", "addWhiteboard")
				.build());

		data = Json.createObjectBuilder().add("name", whiteboardName)
				.add("id", whiteboardId).build();

		jab.add(data);

		JsonArray ja = jab.build();

		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText(ja.toString());

			}
		}
	}

	private void getWhiteboardPostits(Session session, JsonObject data,
			PostitRepository pr) {
		int whiteboardId = Integer.parseInt(data.get("id").toString()
				.replace("\"", ""));

		List<Postit> postits = pr.getAllPostits(whiteboardId);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		for (Postit postit : postits) {
			data = Json.createObjectBuilder().add("method", "allPostits")
					.add("id", postit.getId()).add("title", postit.getTitle())
					.add("information", postit.getInformation())
					.add("color", postit.getColor()).build();
			jab.add(data);
		}

		JsonArray ja = jab.build();

		try {
			session.getBasicRemote().sendText(ja.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void getAllWhiteboards(Session session, WhiteboardRepository wr)
			throws IOException {
		JsonObject jo;
		List<Whiteboard> allWhiteboards = wr.getAllWhiteboards();

		JsonArrayBuilder jab = Json.createArrayBuilder();

		for (Whiteboard whiteboard : allWhiteboards) {
			jo = Json.createObjectBuilder().add("method", "allWhiteboards")
					.add("id", whiteboard.getId())
					.add("name", whiteboard.getName()).build();
			jab.add(jo);
		}

		JsonArray ja = jab.build();

		session.getBasicRemote().sendText(ja.toString());
	}

	private void addPostit(JsonObject jo, PostitRepository pr)
			throws IOException {
		Postit postit = buildPostit(jo);

		int postitId = pr.addProduct(postit);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		jab.add(Json.createObjectBuilder().add("method", "addPostit").build());

		jo = Json.createObjectBuilder().add("title", postit.getTitle())
				.add("information", postit.getInformation())
				.add("color", postit.getColor()).add("id", postitId).build();

		jab.add(jo);

		JsonArray ja = jab.build();

		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText(ja.toString());

			}
		}
	}

	private void deletePostit(JsonObject jo, PostitRepository pr)
			throws IOException {
		int postitId = Integer.parseInt(jo.get("id").toString());

		pr.deletePostit(postitId);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		jab.add(Json.createObjectBuilder().add("method", "deletePostit")
				.build());

		jo = Json.createObjectBuilder().add("id", postitId).build();

		jab.add(jo);

		JsonArray ja = jab.build();


		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText(ja.toString());

			}
		}
	}

	private void updatePostit(JsonObject jo, PostitRepository pr)
			throws IOException {
		Postit postit = buildPostit(jo);

		int postitId = Integer.parseInt(jo.get("id").toString());

		pr.updatePostit(postitId, postit);

		JsonArrayBuilder jab = Json.createArrayBuilder();

		jab.add(Json.createObjectBuilder().add("method", "updatePostit")
				.build());

		jo = Json.createObjectBuilder().add("title", postit.getTitle())
				.add("information", postit.getInformation())
				.add("color", postit.getColor()).add("id", postitId).build();

		jab.add(jo);

		JsonArray ja = jab.build();

		synchronized (clients) {
			for (Session client : clients) {
				client.getBasicRemote().sendText(ja.toString());

			}
		}
	}

	public Postit buildPostit(JsonObject jo) {
		String title = jo.get("title").toString().replace("\"", "");
		String information = jo.get("information").toString().replace("\"", "");
		String color = jo.get("color").toString().replace("\"", "");
		int whiteboardId = Integer.parseInt(jo.get("whiteboard").toString()
				.replace("\"", ""));

		Postit postit = new Postit(title, information, color, whiteboardId);

		return postit;
	}

	@OnClose
	public void myOnClose(Session session, CloseReason reason) {
		System.out.println("Client closed: " + reason);
		clients.remove(session);
	}

	@OnError
	public void myOnError(Session session, Throwable throwable) {
		System.out.println("Client error: " + session);
	}
}
