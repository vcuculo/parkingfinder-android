package mobidev.parkfinder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

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
				.setNegativeButton("Cancel", negativeAction);
		builder.show();
	}
}
