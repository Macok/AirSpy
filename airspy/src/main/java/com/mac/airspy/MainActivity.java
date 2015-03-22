package com.mac.airspy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import com.google.inject.Inject;
import com.mac.airspy.location.BackgroundLocationService;
import com.mac.airspy.location.LocationService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;


public class MainActivity extends RoboActivity {

    @Inject
    private ApplicationController applicationController;

    @Inject
    private AppStateDisplay appStateDisplay;

    @Inject
    private ObjectDetailsDisplay detailsDisplay;

    @Inject
    private RangeComponent rangeComponent;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rangeComponent.init();

        detailsDisplay.init();

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

        appStateDisplay.dismiss();

        super.onDestroy();
    }
}
