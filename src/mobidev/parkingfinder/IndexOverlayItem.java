package mobidev.parkingfinder;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class IndexOverlayItem extends OverlayItem {

	int index;
	
	public IndexOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
		// TODO Auto-generated constructor stub
	}
	
	public IndexOverlayItem(GeoPoint point, String title, String snippet,int index){
		super(point, title, snippet);
		this.index=index;
	}
	
	public int getIndex(){
		return index;
	}
}

