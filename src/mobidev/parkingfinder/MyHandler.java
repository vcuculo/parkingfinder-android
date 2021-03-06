package mobidev.parkingfinder;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapView;

public class MyHandler extends Handler {
	private final static String MY_PREFERENCES = "MyPref";
	private static final String PREFERENCE_AUDIO = "my_audio";
	ArrayList<Parking> parkings;
	MapView map;
	MyItemizedOverlay parkingsOverlay;
	Context c;
	static int nPark = -1;

	public MyHandler(MapView map, MyItemizedOverlay parkingsOverlay) {
		this.map = map;
		this.parkingsOverlay = parkingsOverlay;
		this.c = map.getContext();
	}

	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		if (bundle.containsKey("error")) {
			String value = bundle.getString("error");
			if (value == "ServerError")
				Toast.makeText(c, c.getString(R.string.connectProblem),
						Toast.LENGTH_LONG).show();
		}
		if (bundle.containsKey("refreshParkings")) {
			String value = bundle.getString("refreshParkings");
			try {
				Log.i("Handle_MessageServer", value);
				parkings = DataController.unMarshallParking(value);

				SharedPreferences prefs = c.getSharedPreferences(
						MY_PREFERENCES, Context.MODE_PRIVATE);
				boolean audio = prefs.getBoolean(PREFERENCE_AUDIO, true);
				// controlla se ci sono nuovi parcheggi
				if (parkings.size() > nPark && nPark != -1 && audio) {
					Uri notification = RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Ringtone r = RingtoneManager.getRingtone(
							c.getApplicationContext(), notification);
					r.play();
				}
				nPark = parkings.size();
				parkingsOverlay.clear();

				for (Parking parking : parkings)
					Utility.showParking(map, parking, parkingsOverlay);

				map.invalidate();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
