package com.mac.airspy;

import android.graphics.PointF;
import com.google.inject.Inject;
import com.mac.airspy.location.SimpleLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-11.
 */
public class MainLoop implements Runnable {
    private boolean running = true;

    @Inject
    private ARLayer arLayer;

    @Inject
    private VisibleObjectsObtainer visibleObjectsObtainer;

    @Inject
    private ObjectSourceManager objectSourceManager;

    @Inject
    private FPSCalculator fpsCalculator;

    @Override
    public void run() {
        while (running) {
            arLayer.setFps(fpsCalculator.getFpsAndUpdate());

            if (ComponentState.READY == objectSourceManager.getState()) {
                arLayer.draw(visibleObjectsObtainer.getObjectsOnScreen());
            }

        }
    }

    public void stop() {
        running = false;
    }
}
