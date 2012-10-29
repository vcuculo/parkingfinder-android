package mobidev.parkingfinder;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class ParkingOverlayItem extends OverlayItem {

	private Parking p;
	private boolean release;

	public ParkingOverlayItem(GeoPoint point, String title, String snippet,
			Parking p, boolean release) {
		super(point, title, snippet);
		this.p = p;
		this.release = release;
	}

	public ParkingOverlayItem(GeoPoint point, Parking p, boolean release) {
		super(point, null, null);
		this.p = p;
		this.release = release;
	}

	public Parking getParking() {
		return p;
	}

	public int getId() {
		return p.getId();
	}
	
	public boolean getRelease() {
		return release;
	}
}