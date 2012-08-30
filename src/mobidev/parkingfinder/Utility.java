package mobidev.parkingfinder;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

	private final static String MY_PREFERENCES = "MyPref";

	public static String getDigest(String pw) {
		MessageDigest digester;
		try {
			digester = MessageDigest.getInstance("MD5");
			digester.update(pw.getBytes());
			return String.format("%032x", new BigInteger(1, digester.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static void showDialog(String title, String message, Context c) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(message).setCancelable(false).setTitle(title)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.show();
	}

	public static void showDialog(String title, String message, Context c,
			OnClickListener positiveAction, OnClickListener negativeAction) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(message).setTitle(title)
				.setPositiveButton("Ok", positiveAction)
				.setNegativeButton(R.string.cancel, negativeAction);
		builder.show();
	}

	public static GeoPoint location2geopoint(Location loc) {
		return new GeoPoint((int) (loc.getLatitude() * 1E6),
				(int) (loc.getLongitude() * 1E6));
	}

	public static void centerMap(GeoPoint gp, MapView mapview) {
		MapController mapc = mapview.getController();
		mapview.invalidate();

		List<Overlay> overlays = mapview.getOverlays();

		if (overlays.size() > 1) { // mostrare la mia posizione e quella
									// dell'auto
			MyItemizedOverlay itemizedOverlay = (MyItemizedOverlay) overlays
					.get(1);
			mapc.animateTo(itemizedOverlay.getItem(0).getPoint());
			mapc.setZoom(17);
		} else {
			mapc.animateTo(gp);
			mapc.setZoom(18);
		}
	}

	public static void centerMap(Location loc, MapView mapview) {
		GeoPoint gp = location2geopoint(loc);
		centerMap(gp, mapview);
	}

	public static String getStreetName(Context c, double latitude,
			double longitude) {
		Geocoder geocoder = new Geocoder(c, Locale.ITALIAN);

		try {
			List<Address> addresses = geocoder.getFromLocation(latitude,
					longitude, 1);

			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				String strReturnedAddress = returnedAddress.getAddressLine(0);
				return strReturnedAddress;
			} else {
				return "N/A";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "N/A";
		}
	}

	public static void askParkings(Location myLocation, MapView map) {
		Context c = map.getContext();
		SharedPreferences prefs = c.getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);

		int range = prefs.getInt("range", 100);

		String response;
		try {
			response = CommunicationController.sendRequest("searchParking",
					DataController.marshallParkingRequest(
							myLocation.getLatitude(),
							myLocation.getLongitude(), range));

			ArrayList<Parking> parkings;
			try {
				parkings = DataController.unMarshallParking(response);
				for (Parking parking : parkings)
					showParking(map, parking);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void showParking(MapView mapView, Parking p) {
		Drawable drawable;
		Context c = mapView.getContext();
		switch (p.getType()) {
		case 1:
			drawable = c.getResources().getDrawable(R.drawable.free_park);
			break;
		case 2:
			drawable = c.getResources().getDrawable(R.drawable.toll_park);
			break;
		case 3:
			drawable = c.getResources().getDrawable(R.drawable.reserved_park);
			break;
		case 4:
			drawable = c.getResources().getDrawable(R.drawable.disabled_park);
			break;
		case 5:
			drawable = c.getResources().getDrawable(R.drawable.timed_park);
			break;
		default:
			drawable = c.getResources().getDrawable(R.drawable.undefined_park);
			break;
		}

		MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable, c);
		GeoPoint point = new GeoPoint((int) (p.getLatitude() * 1E6),
				(int) (p.getLongitude() * 1E6));

		long duration = System.currentTimeMillis() - p.getDate().getTime();
		OverlayItem overlayitem = new OverlayItem(point, "Your car", "Lat: "
				+ p.getLatitude() + "\nLon: " + p.getLongitude() + "\nFree: "
				+ TimeUtils.millisToLongDHMS(duration));
		itemizedoverlay.addOverlay(overlayitem);
		mapView.getOverlays().add(itemizedoverlay);
	}
}
