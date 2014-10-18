package com.mac.airspy;

import com.google.inject.Inject;

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
    private ObjectsProvider objectsProvider;

    @Inject
    private FPSCalculator fpsCalculator;

    @Override
    public void run() {
        while (running) {

            if (ComponentState.READY == objectsProvider.getState()) {
                arLayer.setFps(fpsCalculator.getFpsAndUpdate());
                arLayer.draw(visibleObjectsObtainer.getObjectsOnScreen());
            }

        }
    }

    public void stop() {
        running = false;
    }
}
