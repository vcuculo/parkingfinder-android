package mobidev.parkingfinder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

public class MyItemizedOverlay extends ItemizedOverlay<ParkingOverlayItem> {
	private final static String MY_PREFERENCES = "MyPref";
	private final static String ID_KEY = "parkingId";
	private final static String TYPE_KEY = "parkingType";
	private final static String LAT_KEY = "latitude";
	private final static String LON_KEY = "longitude";
	private final static String ACC_KEY = "accuracy";
	private Context mContext;
	private ArrayList<ParkingOverlayItem> mOverlays = new ArrayList<ParkingOverlayItem>();
	private int mSize;

	public MyItemizedOverlay(Context context) {
		super(null);
		mContext = context;
		this.populate();
	}
	
	// create parking overlay received from server
	public void addOverlayItem(Parking p, Long duration, Drawable altMarker) {

		GeoPoint point = new GeoPoint((int) (p.getLatitude() * 1E6),
				(int) (p.getLongitude() * 1E6));

		String[] types = mContext.getResources().getStringArray(
				R.array.parkingTypes);

		String title = types[p.getType()] + " parking";

		String lat = String.format("%.3f", p.getLatitude());
		String lon = String.format("%.3f", p.getLongitude());

		String snippet = "Lat:\t" + lat + "\nLon:\t" + lon + "\nFree since:\t"
				+ TimeUtils.millisToLongDHMS(duration) + "\nComments:\n"
				+ p.getComments();

		ParkingOverlayItem overlayItem = new ParkingOverlayItem(point, title,
				snippet, p, false);
		addOverlayItem(overlayItem, altMarker);
	}

	// create parking overlay received from client (my parking)
	public void addOverlayItem(Parking p, Drawable altMarker) {
		
		GeoPoint point = new GeoPoint((int) (p.getLatitude() * 1E6),
				(int) (p.getLongitude() * 1E6));
		
		ParkingOverlayItem overlayItem = new ParkingOverlayItem(point, p, true);
		addOverlayItem(overlayItem, altMarker);
	}

	public void addOverlayItem(ParkingOverlayItem overlayItem,
			Drawable altMarker) {
		overlayItem.setMarker(boundCenterBottom(altMarker));
		addOverlay(overlayItem);
		populate();
	}

	@Override
	protected ParkingOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mSize;
	}

	@Override
	protected boolean onTap(int index) {
		ParkingOverlayItem item = mOverlays.get(index);
		
		if (item.getRelease())
			return true;
		
		final Parking p = item.getParking();
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		View checkBoxView = View.inflate(mContext, R.layout.checkbox, null);
		final CheckBox checkBox = (CheckBox) checkBoxView
				.findViewById(R.id.checkbox);
		checkBox.setText("Checkin");
		dialog.setView(checkBoxView);
		OnClickListener positive = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				if (checkBox.isChecked())
				new AsyncTaskOccupyPark(mContext, p, true).execute();
				else 
					new AsyncTaskOccupyPark(mContext, p, false).execute();

				SharedPreferences prefs = mContext.getSharedPreferences(
						MY_PREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(ID_KEY, p.getId());
				editor.putFloat(LAT_KEY, (float) p.getLatitude());
				editor.putFloat(LON_KEY, (float) p.getLongitude());
				editor.putFloat(ACC_KEY, p.getAccuracy());
				editor.putInt(TYPE_KEY, p.getType());
				editor.commit();
			}

		};

		dialog.setPositiveButton(R.string.occupy, positive);

		dialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		dialog.show();
		return true;
	}

	public void addOverlay(ParkingOverlayItem overlay) {
		mOverlays.add(overlay);
		setLastFocusedIndex(-1);
		mSize = mOverlays.size();
		populate();
	}
	
	public ArrayList<ParkingOverlayItem> getAll() {
		return mOverlays;
	}

	public void clear() {
		mOverlays.clear();
		setLastFocusedIndex(-1);
		mSize = mOverlays.size();
		populate();
	}
}
