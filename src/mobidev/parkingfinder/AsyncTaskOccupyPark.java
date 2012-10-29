package mobidev.parkingfinder;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AsyncTaskOccupyPark extends AsyncTask<Void, Void, Void> {

	Parking p;
	Context c;
	ProgressDialog pr;
	boolean checkin;

	public AsyncTaskOccupyPark(Context c, Parking p, boolean checkin) {
		this.c = c;
		this.p = p;
		this.checkin = checkin;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			CommunicationController.sendRequest("park",
					DataController.marshallOccupyParking(p.getId()));

		} catch (IOException e) {
			e.printStackTrace();
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
	protected void onPostExecute(Void par) {
		pr.dismiss();
		pr.cancel();
		Activity a = (Activity) c;
		a.finish();
		if (checkin) {
			Intent i = new Intent(c, SocialActivity.class);
			c.startActivity(i);
		}
	}
}