package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class OptionSettingsActivity extends Activity{


	private Spinner parkingTypeSpinner;
	private ToggleButton audioToggleButton,filterToggleButton;
	private SeekBar rangeSeekBar, timeSeekBar;
	private Button saveButton,cancelButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_settings);
		audioToggleButton=(ToggleButton) findViewById(R.id.toggleNotify);
		filterToggleButton=(ToggleButton) findViewById(R.id.toggleFilter);
		rangeSeekBar=(SeekBar)findViewById(R.id.seekBarRange);
		timeSeekBar=(SeekBar)findViewById(R.id.seekBarTime);
		
		parkingTypeSpinner = (Spinner) findViewById(R.id.spinnerFilter);
		cancelButton = (Button) findViewById(R.id.buttonCancel);
		saveButton = (Button) findViewById(R.id.buttonPositive);
		parkingTypeSpinner.setSelection(1);
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


	
	}


}
