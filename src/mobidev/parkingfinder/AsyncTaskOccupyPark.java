package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskOccupyPark extends AsyncTask<Void, Void, Void> {

	Parking p;
	Context c;
	ProgressDialog pr;

	public AsyncTaskOccupyPark(Context c, Parking p) {
		this.c = c;
		this.p = p;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			CommunicationController.sendRequest("park",
					DataController.marshallOccupyParking(p.getId()));

		} catch (IOException e) {
			e.printStackTrace();
			Log.i("occupyParkIOException", "Exception");
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		String s = c.getString(R.string.load);
		String t = c.getString(R.string.app_name);
		pr = ProgressDialog.show(c, t, s, true);
		Log.i("occupyPark", pr.toString());
	}

	@Override
	protected void onPostExecute(Void par) {
		pr.dismiss();
		pr.cancel();
		Log.i("occupyPark", "onpost");
		//Activity a=(Activity)c;
		//a.finish();
	}
}