package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncTaskFreePark extends AsyncTask<Void, Void, Void>{

	Parking p;
	Context c;
	ProgressDialog pr;
	public AsyncTaskFreePark(Context c,Parking p){
		this.c=c;
		this.p=p;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		publishProgress();
		try {
			CommunicationController.sendRequest("freePark", DataController.marshallParking(p));
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("freeParkIOException", "Exception");
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... params){
		String s=c.getString(R.string.load);
		pr = new ProgressDialog(c);
		pr.setMessage(s);
		pr.show();
		Log.i("ProgressUpdate", "park");
	}
	
	public void onPostExecute(ArrayList<Parking> parkings){
		pr.dismiss();
		pr=null;
	}
}
