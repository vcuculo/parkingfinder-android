package mobidev.parkingfinder;

import java.util.Timer;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class PositionController implements LocationListener {

	private Location bestPosition;
	private MapView mapview;
	private static final int HALF_MINUTE = 30 * 1000;
	private boolean first = true;
	private MyLocationOverlay myLocation;
	private Timer t;

	public PositionController(MyLocationOverlay myLoc, MapView map) {
		this.mapview = map;
		this.myLocation = myLoc;
	}

	
	@Override
	public void onLocationChanged(Location arg0) {
		if (isBetterLocation(arg0)) {
			bestPosition = arg0;
			Utility.centerMap(bestPosition, mapview);
			if (myLocation != null && first) {
				Utility.askParkings(bestPosition, mapview);
				t = new Timer();
				t.schedule(new MyTimer(myLocation, mapview), 5000, 5000);
				first = false;
			}
		}
	}

	public void stopTimer(){
		t.cancel();
		t.purge();
	}
	
	public void restartTimer(){
		t = new Timer();
		t.schedule(new MyTimer(myLocation, mapview), 5000, 5000);
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