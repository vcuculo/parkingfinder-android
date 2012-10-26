package mobidev.parkingfinder;

import java.util.TimerTask;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyTimer extends TimerTask {

	private MapView map;
	private MyLocationOverlay myLoc;
	private Handler handler;
	private String JSONParkings;

	public MyTimer(Handler handler, MyLocationOverlay myLoc, MapView map) {
		this.map = map;
		this.myLoc = myLoc;
		this.handler = handler;
	}

	@Override
	public void run() {
		Location myLocation = myLoc.getLastFix();
		if (myLocation != null) {
			JSONParkings = Utility.askParkings(myLocation, map);
			if (JSONParkings != null) {
				Message msg = handler.obtainMessage();
				Bundle b = new Bundle();
				if (JSONParkings == "ServerError") {
					b.putString("error", JSONParkings);
				} else {
					b.putString("refreshParkings", JSONParkings);
				}
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}
	}
}
