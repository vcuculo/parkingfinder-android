package mobidev.parkingfinder;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context mContext;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MyItemizedOverlay(Context context){
		super(null);
		mContext = context;
	}
	
	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public void addOverlayItem(int lat, int lon, String title, String snippet,
			Drawable altMarker) {
		GeoPoint point = new GeoPoint(lat, lon);
		OverlayItem overlayItem = new OverlayItem(point, title, snippet);
		addOverlayItem(overlayItem, altMarker);
	}

	public void addOverlayItem(OverlayItem overlayItem, Drawable altMarker) {
		overlayItem.setMarker(boundCenterBottom(altMarker));
		addOverlay(overlayItem);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
        setLastFocusedIndex(-1);		
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
	
    public void clear() {
    	mOverlays.clear();
        setLastFocusedIndex(-1);        
        populate();
    }
}
