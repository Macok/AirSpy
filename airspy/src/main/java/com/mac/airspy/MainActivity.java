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
    private LocationService locationService;

    private Intent locationServiceIntent;
    private Thread t;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationServiceIntent = new Intent(this, BackgroundLocationService.class);
        startService(locationServiceIntent);

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
        t.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationService.start();
    }

    @Override
    protected void onStop() {
        locationService.stop();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(locationServiceIntent);
        super.onDestroy();
    }
}
