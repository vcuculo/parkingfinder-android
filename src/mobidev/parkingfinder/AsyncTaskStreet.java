package mobidev.parkingfinder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.TextView;

public class AsyncTaskStreet extends AsyncTask<Void, Void, String> {

	Activity c;
	double lat,lon;
	ProgressDialog pr;

	public AsyncTaskStreet(Activity c, double lat, double lon) {
		this.c = c;
		this.lat=lat;
		this.lon=lon;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		publishProgress();
		String street = Utility.getStreetName(c, lat, lon);
		return street;
	}
	@Override
	protected void onProgressUpdate(Void... params){
		String s=c.getString(R.string.load);
		pr = new ProgressDialog(c);
		pr.setMessage(s);
		pr.show();
		Log.i("ProgressUpdate", "street");
	}
	public void onPostExecute(String text){
		pr.dismiss();
		pr=null;
		TextView addressText=(TextView) c.findViewById(R.id.addressValue);
		addressText.setText(text);
	}
}
