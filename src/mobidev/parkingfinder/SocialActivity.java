package mobidev.parkingfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SocialActivity extends Activity 
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylogsocial);
        
        Button btn = (Button)findViewById(R.id.sociallogin);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SocialActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });
    }
}