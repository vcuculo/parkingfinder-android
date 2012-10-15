package mobidev.parkingfinder;

import java.util.Timer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class PositionController implements LocationListener {

	private MapView mapview;
	private final static int MINUTE = 60 * 1000;
	private final static String MY_PREFERENCES = "MyPref";
	private final static String PREFERENCE_REFRESH = "my_refresh";
	private SharedPreferences prefs;

	private boolean release;
	private boolean first = true;
	private MyLocationOverlay myLocation;
	private Handler handler;
	private Timer t = null;
	private ProgressDialog pr;
	private Context c;

	public PositionController(Handler handler, MyLocationOverlay myLoc,
			MapView map, boolean release) {
		this.handler = handler;
		this.mapview = map;
		this.myLocation = myLoc;
		this.release = release;
		this.c = map.getContext();
		prefs = c.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
		String t = c.getString(R.string.app_name);
		String p = c.getString(R.string.loadPosition);
		this.pr = ProgressDialog.show(c, t, p);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		Utility.centerMap(arg0, mapview, release);

		pr.dismiss();
		pr.cancel();

		if (myLocation != null && first) {

			// if (myLocation != null) => Search
			// else => Release

			float refresh = prefs.getFloat(PREFERENCE_REFRESH, 2);
			t = new Timer();
			t.schedule(new MyTimer(handler, myLocation, mapview), 0,
					(int) (refresh * MINUTE));
			first = false;
		}
	}

	public void stopTimer() {
		if (t != null) {
			t.cancel();
			t.purge();
		}
	}

	public void restartTimer() {
		float refresh = prefs.getFloat(PREFERENCE_REFRESH, 2);
		t = new Timer();
		t.schedule(new MyTimer(handler, myLocation, mapview), 0,
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
}