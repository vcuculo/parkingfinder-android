package mobidev.parkingfinder;

import java.util.ArrayList;

import org.json.JSONException;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MyHandler extends Handler {
	
	ArrayList<Parking> parkings;
	MapView map;
	MyItemizedOverlay parkingsOverlay;
	
	public MyHandler(MapView map, MyItemizedOverlay parkingsOverlay) {
		this.map = map;
		this.parkingsOverlay = parkingsOverlay;
	}
	
	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		if (bundle.containsKey("refreshParkings")) {
			String value = bundle.getString("refreshParkings");
			try {
				parkings = DataController.unMarshallParking(value);
				
				parkingsOverlay.clear();

				
				for (Parking parking : parkings)
					Utility.showParking(map, parking, parkingsOverlay);				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
