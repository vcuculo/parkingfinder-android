package mobidev.parkingfinder;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OptionSettingsActivity extends Activity implements OnClickListener{
	private final static String MY_PREFERENCES = "MyPref";
	static final String PREFERENCE_RANGE="my_range";
	static final String PREFERENCE_REFRESH="my_refresh";
	static final String PREFERENCE_AUDIO="my_audio";
	static final String PREFERENCE_FILTER_UNDEFINED="undefined";
	static final String PREFERENCE_FILTER_FREE="free";
	static final String PREFERENCE_FILTER_TOLL="toll";
	static final String PREFERENCE_FILTER_RESIDENT="resident";
	static final String PREFERENCE_FILTER_DISABLED="disabled";
	static final String PREFERENCE_FILTER_TIMED="timed";//("0-1-2-3-4-5")//parse 
	private final int MAX_RANGE_SEEK=800;
	private final float MAX_REFRESH_SEEK=(float)4.5;
	private final int MIN_RANGE_SEEK=200;
	private final float MIN_REFRESH_SEEK=(float)0.5;
	
	private final int FILTER_DIALOG=1;
	
	private ToggleButton audioToggleButton;
	private SeekBar rangeSeekBar, timeSeekBar;
	private Button saveButton,cancelButton,filterButton;
	private TextView valueRange,valueRefresh,valueFilter;
	
	private int currentRange;
	private float currentTime;
	private boolean currentAudio;
	private boolean currentFUndefined,currentFFree,currentFToll,currentFResident;
	private boolean currentFDisabled,currentFTimed;
	
	private int positionRangeCursor,positionTimeCursor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option_settings);
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		currentRange=prefs.getInt(PREFERENCE_RANGE, 300);
		currentTime=prefs.getFloat(PREFERENCE_REFRESH, 3);
		currentAudio=prefs.getBoolean(PREFERENCE_AUDIO, true);
		currentFUndefined=prefs.getBoolean(PREFERENCE_FILTER_UNDEFINED, false);
		currentFFree=prefs.getBoolean(PREFERENCE_FILTER_FREE, false);
		currentFToll=prefs.getBoolean(PREFERENCE_FILTER_TOLL, false);
		currentFResident=prefs.getBoolean(PREFERENCE_FILTER_RESIDENT, false);
		currentFDisabled=prefs.getBoolean(PREFERENCE_FILTER_DISABLED, false);
		currentFTimed=prefs.getBoolean(PREFERENCE_FILTER_TIMED, false);
		
		audioToggleButton=(ToggleButton) findViewById(R.id.toggleNotify);
		rangeSeekBar=(SeekBar)findViewById(R.id.seekBarRange);
		timeSeekBar=(SeekBar)findViewById(R.id.seekBarTime);
		cancelButton = (Button) findViewById(R.id.buttonCancel);
		saveButton = (Button) findViewById(R.id.buttonPositive);
		filterButton=(Button) findViewById(R.id.buttonFilter);
		valueRange=(TextView) findViewById(R.id.textValueRange);
		valueRange.setText(Integer.toString(currentRange));
		valueRefresh=(TextView) findViewById(R.id.textValueTime);
		valueRefresh.setText(Float.toString(currentTime));
		cancelButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		filterButton.setOnClickListener(this);
		audioToggleButton.setChecked(currentAudio);

		//seekbar
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
	
	@Override
	public Dialog onCreateDialog(int id){
		switch (id) {
		case FILTER_DIALOG:
			final CharSequence[] items =getResources().getStringArray(R.array.parkingTypes);
			boolean undefined=(currentFUndefined?true:false);
			boolean free=(currentFFree?true:false);
			boolean toll=(currentFToll?true:false);
			boolean residents=(currentFResident?true:false);
			boolean disabled=(currentFDisabled?true:false);
			boolean timed=(currentFTimed?true:false);
			boolean[] areChecked={undefined,free,toll,residents,disabled,timed};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Check type");
			builder.setMultiChoiceItems(items, areChecked, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:
						if(isChecked)
						currentFUndefined=true;
						else
						currentFUndefined=false;
						break;
					case 1:
						if(isChecked)
						currentFFree=true;
						else
						currentFFree=false;
						break;
					case 2:
						if(isChecked)
						currentFToll=true;
						else
						currentFToll=false;
						break;
					case 3:
						if(isChecked)
						currentFResident=true;
						else
						currentFResident=false;
						break;	
					case 4:
						if(isChecked)
						currentFDisabled=true;
						else
						currentFDisabled=false;
						break;
					case 5:
						if(isChecked)
						currentFTimed=true;
						else
						currentFTimed=false;
						break;
					default:
						break;
					}
				}
			});
			
			AlertDialog alert = builder.create();
			return alert;
			

		default:
			return null;
	
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch (id) {
		case R.id.buttonPositive:
			savePreferences();
			setResult(RESULT_OK);
			this.finish();
			break;
		case R.id.buttonCancel:
			setResult(RESULT_OK);
			this.finish();
			break;
		case R.id.buttonFilter:
			showDialog(FILTER_DIALOG);
			break;
		default:
			break;
		}	
	}

	private void savePreferences(){
		
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREFERENCE_RANGE, currentRange);
		editor.putFloat(PREFERENCE_REFRESH, currentTime);
		currentAudio=audioToggleButton.isChecked();
		editor.putBoolean(PREFERENCE_AUDIO, currentAudio);
		editor.putBoolean(PREFERENCE_FILTER_UNDEFINED, currentFUndefined);
		editor.putBoolean(PREFERENCE_FILTER_FREE, currentFFree);
		editor.putBoolean(PREFERENCE_FILTER_TOLL, currentFToll);
		editor.putBoolean(PREFERENCE_FILTER_RESIDENT, currentFResident);
		editor.putBoolean(PREFERENCE_FILTER_DISABLED, currentFDisabled);
		editor.putBoolean(PREFERENCE_FILTER_TIMED, currentFTimed);
		editor.commit();
	}
	


}
