package com.example.android.habitower;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by PWF
 */

public class SplashActivity extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

/** wait 10 second to skip splash screen**/
        handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        },10000);

/** touch to skip splash screen**/
        RelativeLayout root_layout = (RelativeLayout) findViewById(R.id.root_splash);
        root_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
