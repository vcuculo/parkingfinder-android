package mobidev.parkingfinder;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class ParkingOverlayItem extends OverlayItem {

	Parking p;

	public ParkingOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);

	}

	public ParkingOverlayItem(GeoPoint point, String title, String snippet,
			Parking p) {
		super(point, title, snippet);
		this.p = p;
	}

	public Parking getParking() {
		return p;
	}

	public int getId() {
		return p.getId();
	}
}