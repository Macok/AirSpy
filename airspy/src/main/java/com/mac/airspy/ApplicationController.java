package com.mac.airspy;

import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;

/**
 * Created by Maciej on 2014-10-05.
 */
public class ApplicationController implements ApplicationComponent.StateChangedListener {
    private final LocationService locationService;
    private final OrientationService orientationService;
    private final CameraController cameraController;
    private final ARLayer arLayer;

    private ApplicationComponent[] allComponents;
    private ApplicationComponent[] firstPhaseComponents;

    @Inject
    public ApplicationController(
            LocationService locationService,
            OrientationService orientationService,
            CameraController cameraController,
            ARLayer arLayer
    ) {
        this.locationService = locationService;
        this.orientationService = orientationService;
        this.cameraController = cameraController;
        this.arLayer = arLayer;

        initComponentsLists(locationService, orientationService, cameraController, arLayer);

        for (ApplicationComponent component : allComponents) {
            component.setStateListener(this);
        }
    }

    private void initComponentsLists(LocationService locationService, OrientationService orientationService, CameraController cameraController, ARLayer arLayer) {
        allComponents = new ApplicationComponent[]{
                locationService,
                orientationService,
                cameraController,
                arLayer
        };

        firstPhaseComponents = new ApplicationComponent[]{
                locationService,
                orientationService,
                cameraController,
                arLayer
        };
    }


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

    @Override
    public void onStateChanged(ApplicationComponent c, ComponentState newState) {
        for (ApplicationComponent component : firstPhaseComponents) {
            if (ComponentState.READY != component.getState()) {
                return;
            }
        }

        //start 2 phase if not started
    }
}
