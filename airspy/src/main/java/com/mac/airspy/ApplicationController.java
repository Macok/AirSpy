package com.mac.airspy;

import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;

/**
 * Created by Maciej on 2014-10-05.
 */
public class ApplicationController {
    @Inject
    private LocationService locationService;

    @Inject
    private OrientationService orientationService;

    @Inject
    private CameraController cameraController;

    @Inject
    private ARLayer arLayer;

    public void create() {
        arLayer.init();
    }

    public void start() {
        locationService.start();

        orientationService.start();
    }

    public void resume() {
        cameraController.start();
    }

    public void pause() {
        cameraController.pause();
    }

    public void stop() {
        orientationService.stop();

        locationService.stop();
    }

    public void destroy() {
        arLayer.release();
    }
}
