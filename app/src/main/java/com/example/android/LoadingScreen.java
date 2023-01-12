package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

/**
 * This Activity setups up a view to act as the "Loading" screen
 */
public class LoadingScreen extends AppCompatActivity {

    /**
     * Sets up "Loading Screen" view and switches to {@link LoginActivity} view after
     * 3000ms
     * @param savedInstanceState
     */

    /*
    TODO:
       1. Check internet connection
       2. Load saved settings
       3. Automatically login if remeber me is checked

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        setTitle("KeepUp!");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if we're running on Android 5.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Apply activity transition
                    startActivity(new Intent(LoadingScreen.this, LoginActivity.class));

                } else {
                    // Swap without transition
                    startActivity(new Intent(LoadingScreen.this, LoginActivity.class));

                }

            }
        }, 3000);


    }
}