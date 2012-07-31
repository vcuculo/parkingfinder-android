package mobidev.parkingfinder;

import com.google.android.maps.MapView;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class PositionController implements LocationListener {

	private Location bestPosition;
	private MapView mapview;
	private static final int ONE_MINUTE = 60 * 1000;
	
	public PositionController(MapView map) {
		this.mapview = map;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		if (isBetterLocation(arg0)) {
			bestPosition = arg0;
			Utility.centerMap(bestPosition, mapview);
		}
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
		else if (timedelta > ONE_MINUTE)
			return true;
		else
			return false;
	}	
}