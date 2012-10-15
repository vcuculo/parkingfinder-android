package mobidev.parkingfinder;

import java.util.ArrayList;

public class Parking {
	private int id;
	private double latitude;
	private double longitude;
	private int type;
	private ArrayList<String> comments; //used for s2c 
	private String comment; //used for c2s
	private long time;
	private float accuracy;

	public Parking(int id, double latitude, double longitude, float accuracy) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.accuracy = accuracy;
	}
	
	
	public Parking(int id, double latitude, double longitude, int type,
			ArrayList<String> comments, long time, float accuracy) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.comments = comments;
		this.time=time;
		this.accuracy = accuracy;
	}

	public Parking(double latitude, double longitude, int type,
			ArrayList<String> comments, long time, float accuracy) {
		super();
		this.id = -1;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.comments = comments;
		this.time=time;
		this.accuracy = accuracy;
	}
	
	public Parking(double latitude, double longitude, int type,
			String comment, float accuracy) {
		super();
		this.id = -1;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.comment = comment;
		this.accuracy = accuracy;
	}
	
	public Parking(int id, double latitude, double longitude, int type, float accuracy) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.accuracy = accuracy;
	}
	
	public Parking(int id, double latitude, double longitude, int type,
			String comment, float accuracy) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.comment = comment.trim();
		this.accuracy = accuracy;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<String> getComments() {
		return comments;
	}

	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}

	public long getTime() {	
		return time;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
