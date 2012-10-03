package mobidev.parkingfinder;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
			// Log.i("freePark", "finishrequest");
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
		// Log.i("ProgressUpdate", "freepark");
	}

	@Override
	protected void onPostExecute(Void params) {
		pr.dismiss();
		pr.cancel();
		pr = null;
		Activity a = (Activity) c;
		a.setResult(Activity.RESULT_OK);
	    a.finish();
	}

}