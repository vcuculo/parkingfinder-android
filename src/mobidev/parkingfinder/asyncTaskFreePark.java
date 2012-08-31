package mobidev.parkingfinder;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class asyncTaskFreePark extends AsyncTask<Void, Void, Void>{

	Parking p;
	Context c;
	ProgressDialog pr;
	public asyncTaskFreePark(Context c,Parking p){
		this.c=c;
		this.p=p;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			CommunicationController.sendRequest("freePark", DataController.marshallParking(p));
		} catch (IOException e) {
			e.printStackTrace();
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
	}
}
