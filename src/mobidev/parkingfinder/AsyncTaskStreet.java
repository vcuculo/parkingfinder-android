package mobidev.parkingfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class AsyncTaskStreet extends AsyncTask<Void, Void, String> {

	Activity c;
	double lat, lon;
	ProgressDialog pr;

	public AsyncTaskStreet(Activity c, double lat, double lon) {
		this.c = c;
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	protected String doInBackground(Void... params) {
		String street = Utility.getStreetName(c, lat, lon);
		return street;
	}

	@Override
	protected void onPreExecute() {
		String s = c.getString(R.string.load);
		String t = c.getString(R.string.app_name);
		pr = ProgressDialog.show(c, t, s, true);
		Log.i("ProgressUpdate", "street");
	}

	public void onPostExecute(String text) {
		pr.dismiss();
		pr.cancel();
		TextView addressText = (TextView) c.findViewById(R.id.addressValue);
		addressText.setText(text);
	}
}