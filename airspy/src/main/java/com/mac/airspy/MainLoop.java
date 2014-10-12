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

    private long lastFpsUpdate = 0;
    private int frameCounter = 0;

    @Inject
    private ARLayer arLayer;

    @Inject
    private ScreenPositionCalculator screenPositionCalculator;

    @Inject
    private ObjectSourceManager objectSourceManager;

    @Override
    public void run() {
        while (running) {
            calculateFps();

            List<ObjectOnScreen> objectsOnScreen = new LinkedList<>();
            if (ComponentState.READY == objectSourceManager.getState()) {
                List<ARObject> objects = objectSourceManager.getObjects();

                for (ARObject object : objects) {
                    PointF pointOnScreen = screenPositionCalculator.calculateScreenPosition(object.getLocation());
                    if (pointOnScreen != null) {
                        objectsOnScreen.add(new ObjectOnScreen(object, pointOnScreen));
                    }
                }

            }

            arLayer.draw(objectsOnScreen);
            frameCounter++;
        }
    }

    private void calculateFps() {
        long time = System.currentTimeMillis();

        if (lastFpsUpdate < time - 1000) {
            arLayer.setFps(frameCounter);
            lastFpsUpdate = System.currentTimeMillis();
            frameCounter = 0;
        }
    }

    public void stop() {
        running = false;
    }
}
