package se.sunsette.model;

public class Whiteboard {

	private int id;
	private String name;

	public Whiteboard(int id, String name) {
		this.id = id;
		this.name = name;

	}

	public Whiteboard(String name) {
		this.name = name;

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
