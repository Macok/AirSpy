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

    @Inject
    private ARLayer arLayer;

    @Override
    public void run() {
        ObjectOnScreen o = new ObjectOnScreen(null, new PointF(200, 200));
        List<ObjectOnScreen> objects = new ArrayList<>();
        objects.add(o);

        while (running) {
            arLayer.draw(objects);
        }
    }

    public void stop() {
        running = false;
    }
}
