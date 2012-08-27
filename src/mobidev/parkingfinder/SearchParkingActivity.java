package mobidev.parkingfinder;

import mobidev.parkingfinder.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

public class SearchParkingActivity extends MapActivity {
	
	private MapView mapView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_parking);
		
		mapView = (MapView) findViewById(R.id.mapview);
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
		
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		PositionController locationListener = new PositionController(mapView);

		Criteria fine = new Criteria();
		fine.setAccuracy(Criteria.ACCURACY_FINE);

		locationManager.requestLocationUpdates(
				locationManager.getBestProvider(fine, true), 0, 0,
				locationListener);

		Location lastKnownLocation = locationManager
				.getLastKnownLocation(locationManager.getBestProvider(fine,
						true));

		if (lastKnownLocation != null)
			Utility.centerMap(lastKnownLocation, mapView);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			showGPSDialog();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (!Utility.isOnline(this))
			showConnectionDialog();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void showConnectionDialog() {
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SearchParkingActivity.this.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SearchParkingActivity.this.finish();
			}
		};

		Utility.showDialog("Connection failed",
				getString(R.string.connectionRequired), this, positive,
				negative);
	}

	private void showGPSDialog() {
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SearchParkingActivity.this
						.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		};

		Utility.showDialog("GPS disabled", getString(R.string.gpsDisabled),
				this, positive, negative);
	}
}