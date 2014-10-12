package com.mac.airspy;

import android.graphics.PointF;
import com.google.inject.Inject;

import java.util.ArrayList;
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

    @Override
    public void run() {
        ObjectOnScreen o = new ObjectOnScreen(null, new PointF(200, 200));
        List<ObjectOnScreen> objects = new ArrayList<>();
        objects.add(o);

        while (running) {
            calculateFps();

            arLayer.draw(objects);

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
