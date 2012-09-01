package mobidev.parkingfinder;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context mContext;
	private boolean dialogButton;
	public MyItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	public MyItemizedOverlay(Drawable defaultMarker, Context context,boolean dialog) {
		super(boundCenterBottom(defaultMarker));
		this.dialogButton=dialog;
		mContext = context;
	}


	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

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
		populate();
	}

	@Override
	protected boolean onTap(int index) {
	  ParkingOverlayItem item =(ParkingOverlayItem) mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  if(mContext!=null && dialogButton)
	  dialog.setPositiveButton(R.string.occupyingParking, new MyOnClickListener(mContext,item.getParking()));
	  dialog.show();
	  return true;
	}
}
