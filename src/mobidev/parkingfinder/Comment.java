package mobidev.parkingfinder;

public class Comment {
	private int id;
	private String text;

	public Comment(int d, String t) {
		id = d;
		text = t;
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		return "#" + id + " - " + text;
	}
}
