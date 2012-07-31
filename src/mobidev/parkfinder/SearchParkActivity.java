package mobidev.parkfinder;

import com.google.android.maps.MapActivity;

import android.os.Bundle;

public class SearchParkActivity extends MapActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_parking);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}	
}
