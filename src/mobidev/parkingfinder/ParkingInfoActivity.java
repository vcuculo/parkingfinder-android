package mobidev.parkingfinder;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ParkingInfoActivity extends Activity implements OnClickListener{

	private TextView latitudeText, longitudeText, addressText;
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
		addressText = (TextView) findViewById(R.id.addressValue);
		commentText = (EditText) findViewById(R.id.commentsText);
		parkingTypeSpinner = (Spinner) findViewById(R.id.parkingTypeSpinner);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		saveButton = (Button) findViewById(R.id.saveButton);

		cancelButton.setOnClickListener(this);

		saveButton.setOnClickListener(this);

		i = getIntent();
		lat = i.getDoubleExtra("latitude", 0);
		lon = i.getDoubleExtra("longitude", 0);
		accuracy = i.getFloatExtra("accuracy", 0);
		String street = Utility.getStreetName(this, lat, lon);
		latitudeText.setText(Double.toString(lat));
		longitudeText.setText(Double.toString(lon));
		addressText.setText(street);
		parkingTypeSpinner.setSelection(i.getIntExtra("type", 0));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int idView=v.getId();
		Parking p;
		int id;
		switch (idView) {
		case R.id.saveButton:
		
			id = i.getIntExtra("parkingId", -1);
			int type = parkingTypeSpinner.getSelectedItemPosition();
 			String comment = commentText.getText().toString();
			p = new Parking(id, lat, lon, type, comment, accuracy);
			new asyncTaskFreePark(this, p).execute();
			/*
			try {
				CommunicationController.sendRequest("freePark", DataController.marshallParking(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.cancelButton:
			
			id = i.getIntExtra("parkingId", -1);
			p = new Parking(id, lat, lon, accuracy);
			new asyncTaskFreePark(this, p).execute();
			
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
		
	}
}
