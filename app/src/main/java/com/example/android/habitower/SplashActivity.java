package com.example.android.habitower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by PWF
 */

public class SplashActivity extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

    handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashActivity.this,CatalogActivity.class);
            startActivity(intent);
            finish();
        },300);

    }
}
