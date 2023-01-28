package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import java.util.logging.LoggingMXBean;

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
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setExitTransition(new Slide());
        getWindow().setEnterTransition(new Explode());
        getWindow().setAllowEnterTransitionOverlap(false);
        setContentView(R.layout.activity_loading_screen);
        setTitle("KeepUp!");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                begin();



            }
        }, 3000);


    }

    public void begin()
    {
        Intent TransitionToLogin = new Intent(LoadingScreen.this, LoginActivity.class);
        startActivity(new Intent(LoadingScreen.this, LoginActivity.class));
        startActivity(TransitionToLogin,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}