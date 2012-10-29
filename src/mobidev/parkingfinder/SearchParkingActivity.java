package mobidev.parkingfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SearchParkingActivity extends MapActivity {

	private MapView mapView;
	private ImageButton occupyButton;
	private final static String MY_PREFERENCES = "MyPref";
	private final static String LAT_KEY = "latitude";
	private final static String LON_KEY = "longitude";
	private final static String ACC_KEY = "accuracy";
	private final static String HELP = "help";

	private boolean paused = false;

	private MyLocationOverlay myLocationOverlay;
	private PositionController locationListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		boolean help = prefs.getBoolean(HELP, true);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(HELP, false);
		editor.commit();

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

		MyItemizedOverlay parkingsOverlay = new MyItemizedOverlay(this);
		mapView.getOverlays().add(parkingsOverlay);

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Handler handler = new MyHandler(mapView, parkingsOverlay);

		locationListener = new PositionController(handler, mapView, false);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 0, locationListener);

		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			showGPSDialog();

		if (help)
			showHelp();
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!Utility.isOnline(this))
			showConnectionDialog();
		if (paused) {
			myLocationOverlay.enableMyLocation();
			locationListener.restartTimer();
		}
		paused = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
		locationListener.stopTimer();
		paused = true;
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

	private void showHelp() {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.help);
		dialog.setTitle(getString(R.string.welcome));
		dialog.show();
	}

	private void showConfirmationDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		View checkBoxView = View.inflate(this, R.layout.checkbox, null);
		final CheckBox checkBox = (CheckBox) checkBoxView
				.findViewById(R.id.checkbox);
		final Context mContext = this;
		checkBox.setText("Checkin");
		dialog.setTitle(getString(R.string.occupyingParking));
		dialog.setMessage(getString(R.string.confirmOccupyParking));
		dialog.setView(checkBoxView);
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				Location myLocation = myLocationOverlay.getLastFix();
				editor.putFloat(LAT_KEY, (float) myLocation.getLatitude());
				editor.putFloat(LON_KEY, (float) myLocation.getLongitude());
				editor.putFloat(ACC_KEY, myLocation.getAccuracy());
				editor.commit();
				finish();
				
				OnClickListener positive = new DialogInterface.OnClickListener() {

					Activity a = (Activity) mContext;

					@Override
					public void onClick(DialogInterface dialog, int which) {

						a.finish();
					}

				};
				
				if (checkBox.isChecked()) {
					Intent i = new Intent(mContext, SocialActivity.class);
					startActivity(i);
				} else {
					Utility.showDialog(
							getString(R.string.parkingOccupied),
							getString(R.string.parkedHere), mContext,
							positive);
				}
			}
		};

		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};
        

		
		dialog.setPositiveButton(R.string.occupy, positive);
		dialog.setNegativeButton(R.string.cancel, negative);
		dialog.show();
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
			Utility.centerMap(mapView, false);
			break;
		case R.id.optionsMenu:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			break;
		case R.id.helpMenu:
			showHelp();
			break;
		case R.id.homeMenu:
			finish();
			break;
		}
		return false;
	}
}
