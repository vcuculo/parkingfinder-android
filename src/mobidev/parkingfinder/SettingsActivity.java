package mobidev.parkingfinder;

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
import android.widget.ToggleButton;

public class SettingsActivity extends Activity implements OnClickListener {
	private final static String MY_PREFERENCES = "MyPref";
	private static final String PREFERENCE_RANGE = "my_range";
	private static final String PREFERENCE_REFRESH = "my_refresh";
	private static final String PREFERENCE_AUDIO = "my_audio";
	private static final String PREFERENCE_FILTER_UNDEFINED = "undefined";
	private static final String PREFERENCE_FILTER_FREE = "free";
	private static final String PREFERENCE_FILTER_TOLL = "toll";
	private static final String PREFERENCE_FILTER_RESIDENT = "resident";
	private static final String PREFERENCE_FILTER_DISABLED = "disabled";
	private static final String PREFERENCE_FILTER_TIMED = "timed";
	private final int MAX_RANGE_SEEK = 9;
	private final int MIN_RANGE_SEEK = 1;
	private final float MAX_REFRESH_SEEK = (float) 4.5;
	private final float MIN_REFRESH_SEEK = (float) 0.5;

	private final int FILTER_DIALOG = 1;

	private ToggleButton audioToggleButton;
	private SeekBar rangeSeekBar, timeSeekBar;
	private Button saveButton, cancelButton, filterButton;
	private TextView valueRange, valueRefresh;

	private int currentRange;
	private float currentTime;
	private boolean currentAudio;
	private boolean currentFUndefined, currentFFree, currentFToll,
			currentFResident, currentFDisabled, currentFTimed;

	private int positionRangeCursor, positionTimeCursor;
	private SharedPreferences prefs;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		currentRange = prefs.getInt(PREFERENCE_RANGE, 3);
		currentTime = prefs.getFloat(PREFERENCE_REFRESH, 2);
		currentAudio = prefs.getBoolean(PREFERENCE_AUDIO, true);
		currentFUndefined = prefs.getBoolean(PREFERENCE_FILTER_UNDEFINED, true);
		currentFFree = prefs.getBoolean(PREFERENCE_FILTER_FREE, true);
		currentFToll = prefs.getBoolean(PREFERENCE_FILTER_TOLL, true);
		currentFResident = prefs.getBoolean(PREFERENCE_FILTER_RESIDENT, true);
		currentFDisabled = prefs.getBoolean(PREFERENCE_FILTER_DISABLED, true);
		currentFTimed = prefs.getBoolean(PREFERENCE_FILTER_TIMED, true);

		audioToggleButton = (ToggleButton) findViewById(R.id.toggleNotify);
		rangeSeekBar = (SeekBar) findViewById(R.id.seekBarRange);
		timeSeekBar = (SeekBar) findViewById(R.id.seekBarTime);
		cancelButton = (Button) findViewById(R.id.buttonCancel);
		saveButton = (Button) findViewById(R.id.buttonPositive);
		filterButton = (Button) findViewById(R.id.buttonFilter);
		valueRange = (TextView) findViewById(R.id.textValueRange);
		valueRange.setText(Integer.toString(currentRange));
		valueRefresh = (TextView) findViewById(R.id.textValueTime);
		valueRefresh.setText(Float.toString(currentTime));
		cancelButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		filterButton.setOnClickListener(this);
		audioToggleButton.setChecked(currentAudio);

		positionRangeCursor = (currentRange / 100) - MIN_RANGE_SEEK;
		rangeSeekBar.setMax(MAX_RANGE_SEEK);
		rangeSeekBar.setProgress(positionRangeCursor);
		rangeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentRange = (progress + MIN_RANGE_SEEK) * 100;
				valueRange.setText(Integer.toString(currentRange));
			}
		});

		positionTimeCursor = (int) ((currentTime - MIN_REFRESH_SEEK));
		timeSeekBar.setMax((int) MAX_REFRESH_SEEK);
		timeSeekBar.setProgress(positionTimeCursor);
		timeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentTime = progress + MIN_REFRESH_SEEK;
				valueRefresh.setText(Float.toString(currentTime));
			}
		});
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case FILTER_DIALOG:
			final CharSequence[] items = getResources().getStringArray(
					R.array.parkingTypes);

			boolean[] areChecked = { currentFUndefined, currentFFree,
					currentFToll, currentFResident, currentFDisabled,
					currentFTimed };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.chooseParkingType));
			builder.setMultiChoiceItems(items, areChecked,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							switch (which) {
							case 0:
								currentFUndefined = isChecked;
								break;
							case 1:
								currentFFree = isChecked;
								break;
							case 2:
								currentFToll = isChecked;
								break;
							case 3:
								currentFResident = isChecked;
								break;
							case 4:
								currentFDisabled = isChecked;
								break;
							case 5:
								currentFTimed = isChecked;
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
		int id = v.getId();
		switch (id) {
		case R.id.buttonPositive:
			savePreferences();
			finish();
			break;
		case R.id.buttonCancel:
			finish();
			break;
		case R.id.buttonFilter:
			showDialog(FILTER_DIALOG);
			break;
		default:
			break;
		}
	}

	private void savePreferences() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(PREFERENCE_RANGE, currentRange);
		editor.putFloat(PREFERENCE_REFRESH, currentTime);
		currentAudio = audioToggleButton.isChecked();
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