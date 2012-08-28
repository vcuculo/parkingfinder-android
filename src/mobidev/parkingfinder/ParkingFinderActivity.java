package mobidev.parkingfinder;

import mobidev.parkingfinder.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ParkingFinderActivity extends Activity {

	ImageButton searchButton, releaseButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		searchButton = (ImageButton) findViewById(R.id.searchButton);
		releaseButton = (ImageButton) findViewById(R.id.releaseButton);

		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ParkingFinderActivity.this,
						SearchParkingActivity.class);
				startActivity(i);
			}
		});
		
		releaseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ParkingFinderActivity.this,
						ReleaseParkingActivity.class);
				startActivity(i);
			}
		});
	}

	public void onResume() {
		super.onResume();

		if (!Utility.isOnline(this)) {

			DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ParkingFinderActivity.this.startActivity(new Intent(
							Settings.ACTION_WIRELESS_SETTINGS));
				}
			};

			DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ParkingFinderActivity.this.finish();
				}
			};

			Utility.showDialog("Connection failed",
					getString(R.string.connectionRequired), this, positive,
					negative);
		}
	}
}