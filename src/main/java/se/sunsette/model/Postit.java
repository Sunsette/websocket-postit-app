package se.sunsette.model;

public class Postit {

	private int id;
	private String title;
	private String information;
	private String color;
	private int whiteboardId;

	public Postit(String title, String information, String color,
			int whiteboardId) {
		this.title = title;
		this.information = information;
		this.color = color;
		this.whiteboardId = whiteboardId;
	}

	public Postit(int id, String title, String information, String color,
			int whiteboardId) {
		this.id = id;
		this.title = title;
		this.information = information;
		this.color = color;
		this.whiteboardId = whiteboardId;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getInformation() {
		return information;
	}

	public String getColor() {
		return color;
	}

	public int getWhiteboardId() {
		return whiteboardId;
	}
}
