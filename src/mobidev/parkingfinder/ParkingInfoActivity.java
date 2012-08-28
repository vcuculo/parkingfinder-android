package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

public class ParkingInfoActivity extends Activity {
	
	private TextView latitudeText;
	private TextView longitudeText;
	private Spinner parkingTypeSpinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_info);
		
		latitudeText = (TextView) findViewById(R.id.latitudeValue);
		longitudeText = (TextView) findViewById(R.id.longitudeValue);
		parkingTypeSpinner = (Spinner) findViewById(R.id.parkingTypeSpinner);
		
		Intent i = getIntent();
		int id = i.getIntExtra("parkingId", -1);
		if (id > -1){ // liberiamo un parcheggio conosciuto
			int lat = i.getIntExtra("latitude", 0);
			int lon = i.getIntExtra("longitude", 0);
			latitudeText.setText(Double.toString(lat / 1E6));
			longitudeText.setText(Double.toString(lon / 1E6));
			parkingTypeSpinner.setSelection(i.getIntExtra("type", 0));
		}
	}
}
