package mobidev.parkingfinder;

import java.util.Timer;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class PositionController implements LocationListener {

	private Location bestPosition;
	private MapView mapview;
	private final static int HALF_MINUTE = 30 * 1000;
	private final static String MY_PREFERENCES = "MyPref";
	private final static String PREFERENCE_REFRESH = "my_refresh";

	private boolean first = true;
	private boolean release;
	private MyLocationOverlay myLocation;
	private MyItemizedOverlay parkings;
	private Timer t=null;
	private SharedPreferences prefs;

	public PositionController(MyItemizedOverlay parkings,
			MyLocationOverlay myLoc, MapView map, boolean release) {
		this.parkings = parkings;
		this.mapview = map;
		this.myLocation = myLoc;
		this.release = release;
		Context c = map.getContext();
		prefs = c.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		if (isBetterLocation(arg0)) {
			bestPosition = arg0;
			Utility.centerMap(bestPosition, mapview, release);
			if (myLocation != null && first) {
				float refresh = prefs.getFloat(PREFERENCE_REFRESH, 2);
				t = new Timer();
				t.schedule(new MyTimer(parkings, myLocation, mapview), 0,
						(int) (refresh * 60000));
				first = false;
			}
		}
	}

	public void stopTimer() {
		if(t!=null){
		t.cancel();
		t.purge();
		}
	}

	public void restartTimer() {
		float refresh = prefs.getFloat(PREFERENCE_REFRESH, 2);
		t = new Timer();
		t.schedule(new MyTimer(parkings, myLocation, mapview), 0,
				(int) (refresh * 60000));
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	}

	private boolean isBetterLocation(Location loc) {
		if (bestPosition == null)
			return true;

		long timedelta = loc.getTime() - bestPosition.getTime();

		if (loc.getAccuracy() >= bestPosition.getAccuracy())
			return true;
		else if (timedelta > HALF_MINUTE)
			return true;
		else
			return false;
	}
}