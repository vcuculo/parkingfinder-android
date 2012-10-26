package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SocialActivity extends Activity 
{
	private final static String MY_PREFERENCES = "MyPref";
    private final static String TOKEN="token";
    private String token;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogsocial);
        
        Button btn = (Button)findViewById(R.id.sociallogin);
        SharedPreferences prefs= getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        token=prefs.getString(TOKEN, null);
        if(token ==null){
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SocialActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });
        }else{
        	btn.setVisibility(View.INVISIBLE);
        }
    }
}