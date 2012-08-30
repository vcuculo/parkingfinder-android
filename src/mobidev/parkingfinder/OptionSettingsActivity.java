package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas.EdgeType;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class OptionSettingsActivity extends Activity{
	private final static String MY_PREFERENCES = "MyPref";
	static final String PREFERENCE_RANGE="my_range";
	static final String PREFERENCE_REFRESH="my_refresh";
	static final String PREFERENCE_AUDIO="my_audio";
	static final String PREFERENCE_FILTER="my_filter";//("0-1-2-3-4-5")//parse 
	private final int MAX_RANGE_SEEK=800;
	private final float MAX_REFRESH_SEEK=(float)4.5;
	private final int MIN_RANGE_SEEK=200;
	private final float MIN_REFRESH_SEEK=(float)0.5;
	private ToggleButton audioToggleButton;
	private SeekBar rangeSeekBar, timeSeekBar;
	private Button saveButton,cancelButton;
	private TextView valueRange,valueRefresh;
	
	private int currentRange;
	float currentTime;
	private int positionRangeCursor,positionTimeCursor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_settings);
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		currentRange=prefs.getInt(PREFERENCE_RANGE, 300);
		currentTime=prefs.getFloat(PREFERENCE_REFRESH, 3);
		
		audioToggleButton=(ToggleButton) findViewById(R.id.toggleNotify);
		rangeSeekBar=(SeekBar)findViewById(R.id.seekBarRange);
		timeSeekBar=(SeekBar)findViewById(R.id.seekBarTime);
		cancelButton = (Button) findViewById(R.id.buttonCancel);
		saveButton = (Button) findViewById(R.id.buttonPositive);
		valueRange=(TextView) findViewById(R.id.textValueRange);
		valueRange.setText(Integer.toString(currentRange));
		valueRefresh=(TextView) findViewById(R.id.textValueTime);
		valueRefresh.setText(Float.toString(currentTime));
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
		positionRangeCursor=currentRange-MIN_RANGE_SEEK;
		rangeSeekBar.setMax(MAX_RANGE_SEEK);
		rangeSeekBar.setProgress(positionRangeCursor);
		rangeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentRange=progress+MIN_RANGE_SEEK;
				valueRange.setText(Integer.toString(currentRange));
				
			}
			
			
		
		});
		positionTimeCursor=(int)((currentTime-MIN_REFRESH_SEEK));
		timeSeekBar.setMax((int)MAX_REFRESH_SEEK);
		timeSeekBar.setProgress(positionTimeCursor);
		timeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stubMAX_REFRESH_SEEK/timeSeekBar.getMax())+
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentTime=progress+MIN_REFRESH_SEEK;
				valueRefresh.setText(Float.toString(currentTime));
			}
		});
	}


}
