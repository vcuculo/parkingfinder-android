package mobidev.parkingfinder;

import java.sql.Timestamp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;

public class MyOnClickListener implements OnClickListener {
	private final static String MY_PREFERENCES = "MyPref";
	//parcheggio occupato
	private final static String LAT_KEY_PARK = "latidude";
	private final static String LON_KEY_PARK = "logitude";
	private final static String ACC_KEY_PARK = "accuracy";
	private final static String TYPE_KEY_PARK = "type";
	private final static String ID_KEY_PARK = "parkingId";
	//lo status è 1 occupato
	Parking p;
	Context c;
	
	public MyOnClickListener(Context c,Parking p){
		this.p=p;
		this.c=c;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		showConfirmationDialog();
		
		
	}
	
	private void showConfirmationDialog() {
		OnClickListener positive = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new asyncTaskOccupyPark(c, p).execute();
				SharedPreferences prefs = c.getSharedPreferences(MY_PREFERENCES,
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(ID_KEY_PARK, p.getId());
				editor.putFloat(LAT_KEY_PARK, (float) p.getLatitude());
				editor.putFloat(LON_KEY_PARK, (float)p.getLongitude());
				editor.putInt(TYPE_KEY_PARK,p.getType());
				editor.putFloat(ACC_KEY_PARK, p.getAccuracy());
				editor.commit();
				Activity a=(Activity)c;
				a.finish();
				
			}
		};
		OnClickListener negative = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		};

		Utility.showDialog(c.getString(R.string.occupyingParking),
				c.getString(R.string.confirmOccupyParking), c, positive,
				negative);
	}

}
