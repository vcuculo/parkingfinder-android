package mobidev.parkingfinder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SocialActivity extends Activity {
	private final static String MY_PREFERENCES = "MyPref";
	private final static String TOKEN = "token";
	private final static String LON_KEY = "longitude";
	private final static String LAT_KEY = "latitude";
	private String token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitylogsocial);
		final ListView list = (ListView) findViewById(R.id.listView1);
		Button btn = (Button) findViewById(R.id.sociallogin);
		SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		token = prefs.getString(TOKEN, null);
		if (token == null) {
			btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(SocialActivity.this,
							WebViewActivity.class);
					startActivity(intent);
				}
			});
		} else {
			btn.setVisibility(View.INVISIBLE);
		}
		float lon = prefs.getFloat(LON_KEY, -1);
		float lat = prefs.getFloat(LAT_KEY, -1);
		token = prefs.getString(TOKEN, null);
		String result = null;

		try {
			URL requestURL = new URL(
					"https://api.foursquare.com/v2/venues/search?ll=" + lat
							+ "," + lon + "&oauth_token=" + token);
			result = sendRequest(requestURL, false, null);
			final HashMap<String, String> hm = DataController
					.unMarshallSocial(result);
			Log.i("ArrayList size", Integer.toString(hm.size()));
			ArrayList<String> values;
			values = new ArrayList<String>(hm.keySet());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.row, R.id.rowitem, values);

			// Assign adapter to ListView
			list.setAdapter(adapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String name = (String) list.getItemAtPosition(arg2);
					String venueId = hm.get(name);
					try {
						URL requestURL = new URL(
								"https://api.foursquare.com/v2/checkins/add");
						String data = "venueId="+venueId+"&oauth_token="+token;
						String result = sendRequest(requestURL, true, data);
						Log.i("Checkin", result);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 

				}

			});
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private String sendRequest(URL request, boolean post, String data)
			throws IOException {
		HttpURLConnection conn;
		String result = "";
		conn = (HttpURLConnection) request.openConnection();
		conn.setConnectTimeout(120 * 1000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		if (!post)
			conn.setRequestMethod("GET");
		else {
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(data);
			out.flush();
			out.close();
		}

		// get response
		try {
			result = CommunicationController.readAll(conn.getInputStream());
		} catch (IOException e) {
			result = CommunicationController.readAll(conn.getErrorStream());
		}
		conn.disconnect();
		return result;
	}
}
