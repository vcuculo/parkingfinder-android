package mobidev.parkingfinder;

import java.util.ArrayList;
import java.util.logging.SocketHandler;

import org.json.JSONException;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.maps.MapView;

public class MyHandler extends Handler {
	private final static String MY_PREFERENCES = "MyPref";
	private static final String PREFERENCE_AUDIO = "my_audio";
	ArrayList<Parking> parkings;
	MapView map;
	MyItemizedOverlay parkingsOverlay;
	Context c;
	static int nPark = -1;

	public MyHandler(Context c, MapView map, MyItemizedOverlay parkingsOverlay) {
		this.map = map;
		this.parkingsOverlay = parkingsOverlay;
		this.c = c;
	}

	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		if (bundle.containsKey("refreshParkings")) {
			String value = bundle.getString("refreshParkings");
			try {
				parkings = DataController.unMarshallParking(value);

				SharedPreferences prefs = c.getSharedPreferences(
						MY_PREFERENCES, Context.MODE_PRIVATE);
				boolean audio = prefs.getBoolean(PREFERENCE_AUDIO, true);
				// controlla se ci sono nuovi parcheggi
				if (parkings.size() > nPark && nPark != -1 && audio) {
					MediaPlayer mediaPlayer = MediaPlayer.create(c,
							Notification.DEFAULT_SOUND);
					mediaPlayer.start();
				}
				nPark = parkings.size();
				parkingsOverlay.clear();

				for (Parking parking : parkings)
					Utility.showParking(map, parking, parkingsOverlay);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
