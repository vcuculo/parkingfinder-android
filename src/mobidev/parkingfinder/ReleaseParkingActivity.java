package mobidev.parkingfinder;

import mobidev.parkingfinder.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;

public class ReleaseParkingActivity extends MapActivity {
	private final static String MY_PREFERENCES = "MyPref";
	private final static String ID_KEY = "parkingId";
	private final static String TYPE_KEY = "parkingType";
	private final static String LAT_KEY = "latitude";
	private final static String LON_KEY = "longitude";
	private final static int REQUEST_CODE = 2;

	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private ImageButton releaseButton;
	private int parkingId;
	private int latitude;
	private int longitude;
	private int type;
	private boolean parked = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.release_parking);

		releaseButton = (ImageButton) findViewById(R.id.parkButton);

		releaseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showConfirmationDialog();
			}
		});

		mapView = (MapView) findViewById(R.id.mapview);

		myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		PositionController locationListener = new PositionController(mapView);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			showGPSDialog();

		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		
		/* DEBUG ONLY
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(ID_KEY, 0);
		editor.putInt(TYPE_KEY, 3);
		editor.putInt(LAT_KEY,
				(int) (lastKnownLocation.getLatitude() * 1E6) + 10000);
		editor.putInt(LON_KEY,
				(int) (lastKnownLocation.getLongitude() * 1E6) + 1000);
		editor.commit();
		// DEBUG ONLY */

		longitude = prefs.getInt(LON_KEY, -30); // oceano

		if (longitude != -30) { // abbiamo un parcheggio memorizzato
			parked = true;
			parkingId = prefs.getInt(ID_KEY, -1);
			latitude = prefs.getInt(LAT_KEY, -1);
			type = prefs.getInt(TYPE_KEY, 0);

			Drawable drawable = this.getResources().getDrawable(
					R.drawable.car_icon);
			MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable,
					this);
			GeoPoint point = new GeoPoint(latitude, longitude);
			OverlayItem overlayitem = new OverlayItem(point, "Your car",
					"Lat: " + latitude / 1E6 + "\nLon: " + longitude / 1E6);
			itemizedoverlay.addOverlay(overlayitem);
			mapView.getOverlays().add(itemizedoverlay);
		}

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
				ReleaseParkingActivity.this.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
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
				ReleaseParkingActivity.this
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

	private void showConfirmationDialog() {
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeParkingInfo();
				showReleaseDialog();
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};

		Utility.showDialog(getString(R.string.freeingParking),
				getString(R.string.confirmReleaseParking), this, positive,
				negative);
	}

	private void showReleaseDialog() {
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(ReleaseParkingActivity.this,
						ParkingInfoActivity.class);
				
				if (parked) { // considero le coordinate dell'auto
					i.putExtra("parkingId", parkingId);
					i.putExtra("latitude", latitude);
					i.putExtra("longitude", longitude);
					i.putExtra("type", type);
				}
				else { // considero le coordinate dell'utente
					i.putExtra("latitude", myLocationOverlay.getMyLocation().getLatitudeE6());
					i.putExtra("longitude", myLocationOverlay.getMyLocation().getLongitudeE6());
				}
				startActivityForResult(i, REQUEST_CODE);
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (parked)
					finish();
				else {
					// TODO : send latitude/longitude to the server
					finish();
				}
			}
		};

		Utility.showDialog(getString(R.string.freeingParking),
				getString(R.string.addParkInformation), this, positive,
				negative);
	}

	private void removeParkingInfo() {
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(ID_KEY);
		editor.remove(LAT_KEY);
		editor.remove(LON_KEY);
		editor.remove(TYPE_KEY);
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				finish();
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);

	}
}
