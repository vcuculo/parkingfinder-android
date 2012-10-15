package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ParkingInfoActivity extends Activity {

	private TextView latitudeText, longitudeText;
	private EditText commentText;
	private Spinner parkingTypeSpinner;
	private Button cancelButton, saveButton;
	private Intent i;
	private double lat, lon;
	private float accuracy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_info);

		latitudeText = (TextView) findViewById(R.id.latitudeValue);
		longitudeText = (TextView) findViewById(R.id.longitudeValue);
		commentText = (EditText) findViewById(R.id.commentsText);
		parkingTypeSpinner = (Spinner) findViewById(R.id.parkingTypeSpinner);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		saveButton = (Button) findViewById(R.id.saveButton);

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Parking p;
				int id = i.getIntExtra("parkingId", -1);
				p = new Parking(id, lat, lon, accuracy);

				new AsyncTaskFreePark(ParkingInfoActivity.this, p).execute();

			}
		});

		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Parking p;
				int id = i.getIntExtra("parkingId", -1);
				int type = parkingTypeSpinner.getSelectedItemPosition();
				String comment = commentText.getText().toString();
				p = new Parking(id, lat, lon, type, comment, accuracy);

				new AsyncTaskFreePark(ParkingInfoActivity.this, p).execute();
			}
		});

		i = getIntent();
		lat = i.getDoubleExtra("latitude", 0);
		lon = i.getDoubleExtra("longitude", 0);
		accuracy = i.getFloatExtra("accuracy", 0);
		new AsyncTaskStreet(this, lat, lon).execute();
		latitudeText.setText(Double.toString(lat));
		longitudeText.setText(Double.toString(lon));
		parkingTypeSpinner.setSelection(i.getIntExtra("type", 0));
	}
}
