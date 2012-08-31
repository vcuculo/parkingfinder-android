package mobidev.parkingfinder;

import java.util.TimerTask;

import android.location.Location;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyTimer extends TimerTask {

	private MapView map;
	private MyLocationOverlay myLoc;

	public MyTimer(MyLocationOverlay myLoc, MapView map) {
		this.map = map;
		this.myLoc = myLoc;
	}

	@Override
	public void run() {
		Location myLocation = myLoc.getLastFix();
		if (myLocation != null)
			Utility.askParkings(myLocation, map);
	}
}
