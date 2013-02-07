package mobidev.parkingfinder;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskFreePark extends AsyncTask<Void, Void, Void> {

	Parking p;
	Context c;
	ProgressDialog pr;

	public AsyncTaskFreePark(Context c, Parking p) {
		this.c = c;
		this.p = p;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			CommunicationController.sendRequest("freePark",
					DataController.marshallParking(p));
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("freeParkIOException", "Exception");
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		String s = c.getString(R.string.load);
		String t = c.getString(R.string.app_name);
		pr = ProgressDialog.show(c, t, s, true);
	}

	@Override
	protected void onPostExecute(Void params) {
		pr.dismiss();
		pr.cancel();
		pr = null;
		Activity a = (Activity) c;
		if (a instanceof ParkingInfoActivity) {
			a.setResult(Activity.RESULT_OK);
			a.finish();
		} else {
			OnClickListener closeAction = new DialogInterface.OnClickListener() {
				Activity a = (Activity) c;

				public void onClick(DialogInterface dialog, int id) {
					a.finish();
				}
			};
			Utility.showDialog(c.getString(R.string.parkingReleased),
					c.getString(R.string.thanks), c, closeAction);
		}
	}
}