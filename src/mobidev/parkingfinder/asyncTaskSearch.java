package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.maps.MapView;

public class asyncTaskSearch extends AsyncTask<Void, Void, ArrayList<Parking>> {
private MapView map;
private Location myLocation;
private float range;
private Context context;
	public asyncTaskSearch(Context context,MapView map,Location myLocation,float range){
		this.map=map;
		this.myLocation=myLocation;
		this.range=range;
		this.context=context;
	}
	
	@Override
	protected ArrayList<Parking> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response;
		ArrayList<Parking> parkings=null;
		try {
			response = CommunicationController.sendRequest("searchParking",
					DataController.marshallParkingRequest(
							myLocation.getLatitude(),
							myLocation.getLongitude(), range));
			try {

				if (!map.getOverlays().isEmpty())
					//map.getOverlays().removeAll(map.getOverlays().subList(1, map.getOverlays().size()));

				/*MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
						map.getContext(), map);
				map.getOverlays().add(myLocationOverlay);
				myLocationOverlay.enableMyLocation();*/

				Log.i("SIZE", String.valueOf(map.getOverlays().size()));
				parkings = DataController.unMarshallParking(response);
				
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return parkings;
	}

	public void onPostExecute(ArrayList<Parking> parkings){
		for (Parking parking : parkings)
			Utility.showParking(map, parking);
	}
}
