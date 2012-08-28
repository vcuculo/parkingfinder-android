package mobidev.parkingfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SearchParkingActivity extends MapActivity implements android.view.View.OnClickListener{
	
	static final int DIALOG_OCCUPIED=0;
	
	private MapView mapView;
	private ImageView centerPosition;
	private Button occupyButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_parking);
		
		centerPosition=(ImageView) findViewById(R.id.centerButtonImage);
		occupyButton=(Button) findViewById(R.id.parkButton);
		
		centerPosition.setOnClickListener(this);
		occupyButton.setOnClickListener(this);
		
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
	


	@Override
	public void onClick(View v) {
	int id=v.getId();
	switch (id) {
	case R.id.parkButton:
		showDialog(DIALOG_OCCUPIED);
		break;
	case R.id.positiveButton:
		finish();
		break;
	default:
		break;
	}
	
	}
	

	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    AlertDialog.Builder builder;
	    AlertDialog alertDialog;
	    switch(id) {
	    case DIALOG_OCCUPIED:
	    	/*Context mContext = getApplicationContext();
	    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
	    	View layout = inflater.inflate(R.layout.occupieddialog,
                    (ViewGroup) findViewById(R.id.occupied_dialog));

	    	Button positive = (Button) layout.findViewById(R.id.positiveButton);
	    	positive.setOnClickListener(this);
	    	
	    	builder=new AlertDialog.Builder(mContext);
	    	builder.setView(layout);
	    	alertDialog=builder.create();
	    	return alertDialog;
	    */
	    builder = new AlertDialog.Builder(this);
			// Codice di creazione del dialog
	    	builder.setTitle(R.string.titleOccupiedDilaog);
			builder.setMessage(R.string.occupiedParking);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			return builder.create();
	   
	    default:
	        dialog = null;
	    }
	    return dialog;
	}	
	   
	}

	

