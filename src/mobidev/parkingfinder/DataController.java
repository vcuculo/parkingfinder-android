package mobidev.parkingfinder;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Point;
import android.util.Log;

public class DataController {

	/* Used to send a freed parking */
	public static String marshallParking(Parking p) {
		JSONObject userJS = new JSONObject();
		try {
			userJS.put("lat", p.getLatitude());
			userJS.put("lon", p.getLongitude());
			userJS.put("accuracy", p.getAccuracy());
			userJS.put("type", p.getType());
			if (p.getId() > -1)
				userJS.put("id", p.getId());
			if (p.getComment() != null)
				userJS.putOpt("text", p.getComment());
			return userJS.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	/* Used to request a list of parking */
	public static String marshallParkingRequest(double lat, double lon,
			float range) {
		JSONObject request = new JSONObject();

		try {
			request.put("lat", lat);
			request.put("lon", lon);
			request.put("range", range);
			return request.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	/* Response from server with a list of available parking */
	public static ArrayList<Parking> unMarshallParking(String response)
			throws JSONException {
		JSONObject parking = new JSONObject(response);
		JSONArray jsonParking = parking.getJSONArray("parking");
		ArrayList<Parking> parkingList = new ArrayList<Parking>();
		int length = jsonParking.length();
		for (int i = 0; i < length; i++) {
			JSONObject park = jsonParking.getJSONObject(i);
			int id = park.getInt("id");
			double lat = park.getDouble("lat");
			double lon = park.getDouble("lon");
			int type = park.getInt("type");
			int accuracy = park.getInt("accuracy");
			long time = park.getLong("date");
			ArrayList<Comment> comments = new ArrayList<Comment>();
			if (park.has("comments")) {
				JSONArray messageJson = park.getJSONArray("comments");
				for (int y = 0; y < messageJson.length(); y++) {
					JSONObject message = messageJson.getJSONObject(y);
					int cId = message.getInt("comment_id");
					String text = message.getString("text");
					Comment c = new Comment(cId, text);
					comments.add(c);
				}
			}
			Parking p = new Parking(id, lat, lon, type, comments, time,
					accuracy);
			parkingList.add(p);
		}
		return parkingList;
	}

	/* Used to occupy a parking */
	public static String marshallOccupyParking(int id) {
		JSONObject request = new JSONObject();
		try {
			request.put("id", id);
			return request.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static HashMap<String, String> unMarshallSocial(String s)
			throws JSONException {
		JSONObject social = new JSONObject(s);
		JSONObject response = social.getJSONObject("response");
		JSONArray groups = response.getJSONArray("groups");
		JSONObject group = groups.getJSONObject(0);
		JSONArray items = group.getJSONArray("items");
		HashMap<String, String> hm = new HashMap<String, String>();

		int len = items.length();
		for (int i = 0; i < len; i++) {
			JSONObject point = items.getJSONObject(i);
			String id = point.getString("id");
			String name = point.getString("name");
			hm.put(name, id);
		}
		return hm;
	}
	public static String marshallCheckin(String id, String token) throws JSONException{
		JSONObject checkin = new JSONObject();
		checkin.put("oauth_token", token);
		checkin.put("venueId", id);
		return checkin.toString();
	}
	
}
