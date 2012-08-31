package mobidev.parkingfinder;

import java.util.TimerTask;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Looper;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyTimer extends TimerTask {

	private MapView map;
	private MyLocationOverlay myLoc;
    private MyItemizedOverlay parkings;
	
	public MyTimer(MyItemizedOverlay parkings, MyLocationOverlay myLoc, MapView map) {
		this.map = map;
		this.myLoc = myLoc;
		this.parkings = parkings;
	}

	@Override
	public void run() {
		
		Location myLocation = myLoc.getLastFix();
		if (myLocation != null)
			Utility.askParkings(myLocation, map, parkings);
	}
}
