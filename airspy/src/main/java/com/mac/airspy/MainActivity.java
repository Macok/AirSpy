package com.mac.airspy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.inject.Inject;
import com.mac.airspy.location.BackgroundLocationService;
import com.mac.airspy.location.LocationService;
import roboguice.activity.RoboActivity;


public class MainActivity extends RoboActivity {

    @Inject
    private ApplicationController applicationController;

    private Thread t;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
/*
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i=0;
                    while (i++<50) {
                        Log.d("", "STATE: " + locationService.getState());
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
        t.start();*/

        applicationController.create();
    }

    @Override
    protected void onStart() {
        super.onStart();

        applicationController.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        applicationController.resume();
    }

    @Override
    protected void onPause() {
        applicationController.pause();

        super.onPause();
    }

    @Override
    protected void onStop() {
        applicationController.stop();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        applicationController.destroy();

        super.onDestroy();
    }
}
