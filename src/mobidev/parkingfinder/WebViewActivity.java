package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	private static final String TAG = "ActivityWebView";

	public static final String CALLBACK_URL = "http://myapp.com";
	public static final String CLIENT_ID = "AEAEBVBXOPC0IQNS4K1THAGNC5RFTE2V4GADSD3DQQUS5UAF";
	private final static String MY_PREFERENCES = "MyPref";
	private final static String TOKEN = "token";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weblogin);
		String url = "https://foursquare.com/oauth2/authenticate"
				+ "?client_id=" + CLIENT_ID + "&response_type=token"
				+ "&redirect_uri=" + CALLBACK_URL;

		// If authentication works, we'll get redirected to a url with a pattern
		// like:
		//
		// http://YOUR_REGISTERED_REDIRECT_URI/#access_token=ACCESS_TOKEN
		//
		// We can override onPageStarted() in the web client and grab the token
		// out.
		WebView webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				String fragment = "#access_token=";
				int start = url.indexOf(fragment);
				if (start > -1) {
					// You can use the accessToken for api calls now.
					String accessToken = url.substring(
							start + fragment.length(), url.length());
					SharedPreferences prefs = getSharedPreferences(
							MY_PREFERENCES, Context.MODE_PRIVATE);
					SharedPreferences.Editor edit = prefs.edit();
					edit.putString(TOKEN, accessToken);
					edit.commit();
					Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");

					finish();
					Intent intent = new Intent(WebViewActivity.this,
							SocialActivity.class);
					startActivity(intent);

				}
			}
		});
		webview.loadUrl(url);
	}
}