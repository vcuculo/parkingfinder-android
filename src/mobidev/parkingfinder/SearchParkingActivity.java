package mobidev.parkingfinder;

import mobidev.parkingfinder.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class SearchParkingActivity extends MapActivity {

	private MapView mapView;
	private ImageButton occupyButton;
	private final static String MY_PREFERENCES = "MyPref";
	private final static String LAT_KEY = "latitude";
	private final static String LON_KEY = "longitude";
	private final static int REQUEST_CODE = 1;
	private MyLocationOverlay myLocationOverlay;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_parking);

		occupyButton = (ImageButton) findViewById(R.id.parkButton);

		occupyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showConfirmationDialog();
			}
		});

		mapView = (MapView) findViewById(R.id.mapview);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		PositionController locationListener = new PositionController(mapView);

		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 0, locationListener);

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

	private void showConfirmationDialog() {
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(LAT_KEY, myLocationOverlay.getMyLocation()
						.getLatitudeE6());
				editor.putInt(LON_KEY, myLocationOverlay.getMyLocation()
						.getLongitudeE6());
				editor.commit();
				finish();
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};

		Utility.showDialog(getString(R.string.occupyingParking),
				getString(R.string.confirmOccupyParking), this, positive,
				negative);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.centerMenu:
			Utility.centerMap(myLocationOverlay.getMyLocation(), mapView);
			break;
		case R.id.optionsMenu:
			// TODO: Options
			Intent i=new Intent(this,OptionSettingsActivity.class);
			startActivityForResult(i, REQUEST_CODE);
			break;
		case R.id.exitMenu:
			finish();
			break;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE) {
			switch (resultCode) {
			case RESULT_OK:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);

	}
}
