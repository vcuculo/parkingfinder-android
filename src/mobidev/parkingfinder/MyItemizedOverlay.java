package mobidev.parkingfinder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

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
		populate();
	}

	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		populate();
	}

	public void addOverlayItem(Parking p, Long duration, Drawable altMarker) {

		GeoPoint point = new GeoPoint((int) (p.getLatitude() * 1E6),
				(int) (p.getLongitude() * 1E6));

		String title = "#" + p.getId();

		String[] types = mContext.getResources().getStringArray(
				R.array.parkingTypes);

		String snippet = "Lat:\t " + p.getLatitude() + "\nLon:\t"
				+ p.getLongitude() + "\nFree since:\t"
				+ TimeUtils.millisToLongDHMS(duration) + "\nType:\t"
				+ types[p.getType()] + "\nComments:\n" + p.getComments();

		ParkingOverlayItem overlayItem = new ParkingOverlayItem(point, title,
				snippet, p);
		addOverlayItem(overlayItem, altMarker);
		populate();
	}

	public void addOverlayItem(int lat, int lon, String title, String snippet,
			Drawable altMarker) {
		GeoPoint point = new GeoPoint(lat, lon);
		ParkingOverlayItem overlayItem = new ParkingOverlayItem(point, title,
				snippet);
		addOverlayItem(overlayItem, altMarker);
	}

	public void addOverlayItem(ParkingOverlayItem overlayItem,
			Drawable altMarker) {
		overlayItem.setMarker(boundCenterBottom(altMarker));
		addOverlay(overlayItem);
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
		final Parking p = item.getParking();

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());

		OnClickListener positive = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				new AsyncTaskOccupyPark(mContext, p).execute();
				SharedPreferences prefs = mContext.getSharedPreferences(
						MY_PREFERENCES, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt(ID_KEY, p.getId());
				editor.putFloat(LAT_KEY, (float) p.getLatitude());
				editor.putFloat(LON_KEY, (float) p.getLongitude());
				editor.putFloat(ACC_KEY, p.getAccuracy());
				editor.putInt(TYPE_KEY, p.getType());
				editor.commit();
				
				OnClickListener positive = new DialogInterface.OnClickListener() {
					Activity a = (Activity) mContext;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						a.finish();
					}
				};
								
				Utility.showDialog(
						mContext.getString(R.string.parkingOccupied),
						mContext.getString(R.string.parkedHere), mContext, positive);
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

	public void clear() {
		mOverlays.clear();
		setLastFocusedIndex(-1);
		mSize = mOverlays.size();
		populate();
	}
}
