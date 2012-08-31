package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ParkingInfoActivity extends Activity {

	private TextView latitudeText, longitudeText, addressText;
	private Spinner parkingTypeSpinner;
	private Button cancelButton, saveButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_info);

		latitudeText = (TextView) findViewById(R.id.latitudeValue);
		longitudeText = (TextView) findViewById(R.id.longitudeValue);
		addressText = (TextView) findViewById(R.id.addressValue);
		parkingTypeSpinner = (Spinner) findViewById(R.id.parkingTypeSpinner);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		saveButton = (Button) findViewById(R.id.saveButton);

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO : send info to the server
				setResult(RESULT_OK);
				finish();
			}
		});

		Intent i = getIntent();
		//int id = i.getIntExtra("parkingId", -1);
		int lat = i.getIntExtra("latitude", 0);
		int lon = i.getIntExtra("longitude", 0);
		String street = Utility.getStreetName(this, lat / 1E6, lon / 1E6);
		latitudeText.setText(Double.toString(lat / 1E6));
		longitudeText.setText(Double.toString(lon / 1E6));
		addressText.setText(street);
		parkingTypeSpinner.setSelection(i.getIntExtra("type", 0));
	}
}
