package mobidev.parkingfinder;

import java.sql.Timestamp;

public class Parking {
	private int id;
	private int latitude;
	private int longitude;
	private int type;
	private String[] comments;
	private Timestamp date;
	private int accuracy;

	public Parking(int id, int latitude, int longitude, int type,
			String[] comments, Timestamp date, int accuracy) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.comments = comments;
		this.date = date;
		this.accuracy = accuracy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String[] getComments() {
		return comments;
	}

	public void setComments(String[] comments) {
		this.comments = comments;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
}
