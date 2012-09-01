package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class asyncTaskOccupyPark extends AsyncTask<Void, Void, Void> {
	
	Parking p;
	Context c;
	ProgressDialog pr;
	public asyncTaskOccupyPark(Context c,Parking p){
		this.c=c;
		this.p=p;
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		publishProgress();
		try {
			CommunicationController.sendRequest("park", DataController.marshallOccupyParking(p.getId()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("freeParkIOException", "Exception");
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Void... params){
		String s=c.getString(R.string.load);
		pr = ProgressDialog.show(c, "", "Loading. Please wait...", true);
	}
	
	public void onPostExecute(ArrayList<Parking> parkings){
		pr.dismiss();
		pr.cancel();
	}
}
