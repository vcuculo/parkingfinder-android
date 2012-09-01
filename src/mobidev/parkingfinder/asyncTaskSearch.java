package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapView;

public class asyncTaskSearch extends AsyncTask<Void, Void, ArrayList<Parking>> {
	private final static String MY_PREFERENCES = "MyPref";
	private final static String PREFERENCES_SEARCH_PARK = "search";
	private MapView map;
	private Location myLocation;
	private float range;
	private Context context;
	boolean error = false;
	ProgressDialog pr = null;
	static int lengthParkingList=Integer.MAX_VALUE;

	public asyncTaskSearch(Context context, MapView map, Location myLocation,
			float range) {
		this.map = map;
		this.myLocation = myLocation;
		this.range = range;
		this.context = context;
	}

	@Override
	protected ArrayList<Parking> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response;
		ArrayList<Parking> parkings = null;

		try {
			response = CommunicationController.sendRequest("searchParking",
					DataController.marshallParkingRequest(
							myLocation.getLatitude(),
							myLocation.getLongitude(), range));
			try {

				if (!map.getOverlays().isEmpty())
					// map.getOverlays().removeAll(map.getOverlays().subList(1,
					// map.getOverlays().size()));

					/*
					 * MyLocationOverlay myLocationOverlay = new
					 * MyLocationOverlay( map.getContext(), map);
					 * map.getOverlays().add(myLocationOverlay);
					 * myLocationOverlay.enableMyLocation();
					 */

					Log.i("SIZE", String.valueOf(map.getOverlays().size()));
				parkings = DataController.unMarshallParking(response);
				Log.i("Json", response);

			} catch (JSONException e) {
				e.printStackTrace();
				Log.i("Json", "Exception");
				error = true;

			}
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.i("IOExceprtion", "Exception");
			error = true;

		}
		return parkings;
	}

	public void onPostExecute(ArrayList<Parking> parkings) {
		String text = context.getString(R.string.connectProblem);
		if (error) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			error = false;
			SharedPreferences prefs = context.getSharedPreferences(MY_PREFERENCES,
					Context.MODE_PRIVATE);
			SharedPreferences.Editor edit=prefs.edit();
			edit.putBoolean(PREFERENCES_SEARCH_PARK, false);
			edit.commit();
			
		}
		if (parkings == null)
			return;
		for (Parking parking : parkings)
			Utility.showParking(map, parking);
			lengthParkingList=parkings.size();
			if(parkings.size()>lengthParkingList)
				Utility.createNotification(R.drawable.car_icon, "New Park", context, true);
			
			

	}
}
