package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class asyncTaskOccupyPark extends AsyncTask<Void, Void, Void> {
	
	int idParking;
	Context c;
	ProgressDialog pr;
	public asyncTaskOccupyPark(Context c,int idParking){
		this.c=c;
		this.idParking=idParking;
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		publishProgress();
		try {
			CommunicationController.sendRequest("park", DataController.marshallOccupyParking(idParking));
			
			
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
		pr.cancel();
		pr=null;
	}
}
