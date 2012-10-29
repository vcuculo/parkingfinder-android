package mobidev.parkingfinder;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

public class AsyncTaskCheckin extends AsyncTask<Void, Void, String> {

	Activity c;
	ProgressDialog pr;
	String command, data;
	boolean post;

	public AsyncTaskCheckin(Activity c, String command, boolean post,
			String data) {
		this.c = c;
		this.command = command;
		this.post = post;
		this.data = data;
	}

	@Override
	protected String doInBackground(Void... params) {
		String result = null;
		try {
			result = CommunicationController.sendFourSquareRequest(command,
					post, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPreExecute() {
		String s = c.getString(R.string.load);
		String t = c.getString(R.string.app_name);
		pr = ProgressDialog.show(c, t, s, true);
	}

	public void onPostExecute(String result) {
		pr.dismiss();
		pr.cancel();
		OnClickListener positive = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				c.finish();
			}
		};
		Utility.showDialog("Foursquare", c.getString(R.string.checkedin), c, positive);
	}
}