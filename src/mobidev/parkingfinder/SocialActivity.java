package mobidev.parkingfinder;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SocialActivity extends Activity {
	private final static String MY_PREFERENCES = "MyPref";
	private final static String TOKEN = "token";
	private final static String LON_KEY = "longitude";
	private final static String LAT_KEY = "latitude";
	private String token;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitylogsocial);
		Button btn = (Button) findViewById(R.id.sociallogin);
		prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
		token = prefs.getString(TOKEN, null);
		
		if (token == null) {
			btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					Intent intent = new Intent(SocialActivity.this,
							WebViewActivity.class);
					startActivity(intent);
				}
			});
		} else {
			btn.setVisibility(View.INVISIBLE);

			float lon = prefs.getFloat(LON_KEY, -1);
			float lat = prefs.getFloat(LAT_KEY, -1);
			token = prefs.getString(TOKEN, null);

			String command = "venues/search";
			String data = "?ll=" + lat + "," + lon + "&oauth_token=" + token;
			new AsyncTaskFoursquare(this, command, false, data).execute();
		}
	}

	public void addVenues(String result) {
		final HashMap<String, String> hm;
		try {
			hm = DataController.unMarshallSocial(result);

			final ListView list = (ListView) findViewById(R.id.listView1);

			ArrayList<String> keys = new ArrayList<String>(hm.keySet());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.row, R.id.rowitem, keys);

			// Assign adapter to ListView
			list.setAdapter(adapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String name = (String) list.getItemAtPosition(arg2);
					String venueId = hm.get(name);
					String command = "checkins/add";
					String data = "venueId=" + venueId + "&oauth_token=" + token;

					new AsyncTaskCheckin(SocialActivity.this, command, true,
							data).execute();
				}
			});
		} catch (JSONException e2) {
			Toast.makeText(this, getString(R.string.networkProblem), Toast.LENGTH_LONG).show();
			Editor editor = prefs.edit();
			editor.remove(TOKEN);
			editor.commit();
		}
	}
}
