package com.mac.airspy;

import android.graphics.PointF;
import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-05.
 */

@ContextSingleton
public class ApplicationController extends BaseApplicationComponent
        implements ApplicationComponent.StateChangedListener {

    private final LocationService locationService;
    private final OrientationService orientationService;
    private final CameraController cameraController;
    private final ARLayer arLayer;
    private final MainLoopController mainLoopController;
    private final ObjectSourceManager objectSourceManager;

    private ApplicationComponent[] allComponents;
    private ApplicationComponent[] firstPhaseComponents;

    //component that currently makes app state ERROR or STARTING
    private ApplicationComponent blockingComponent;

    private boolean pausedByUser;

    @Inject
    public ApplicationController(
            LocationService locationService,
            OrientationService orientationService,
            CameraController cameraController,
            ARLayer arLayer,
            MainLoopController mainLoopController,
            ObjectSourceManager objectSourceManager
    ) {
        this.locationService = locationService;
        this.orientationService = orientationService;
        this.cameraController = cameraController;
        this.arLayer = arLayer;
        this.mainLoopController = mainLoopController;
        this.objectSourceManager = objectSourceManager;

        initComponentsLists();

        for (ApplicationComponent component : allComponents) {
            component.setStateListener(this);
        }
    }

    private void initComponentsLists() {
        allComponents = new ApplicationComponent[]{
                locationService,
                orientationService,
                cameraController,
                arLayer,
                mainLoopController,
                objectSourceManager
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
        pausedByUser = false;

        cameraController.resume();
    }

    public void pause() {
        pausedByUser = true;

        cameraController.pause();

        if (ComponentState.STOPPED != mainLoopController.getState()) {
            mainLoopController.stop();
        }

        if (ComponentState.STOPPED != objectSourceManager.getState()) {
            objectSourceManager.stop();
        }
    }

    public void stop() {
        orientationService.stop();

        locationService.stop();
    }

    public void destroy() {
        arLayer.release();
    }

    @Override
    public void onStateChanged(ApplicationComponent component, ComponentState newState) {
        updateAppState();

        if (firstPhaseReady() && !pausedByUser) {
            if (!componentStarted(mainLoopController)) {
                mainLoopController.start();
            }

            if (!componentStarted(objectSourceManager)) {
                objectSourceManager.start();
            }
        }
    }

    private boolean componentStarted(ApplicationComponent component) {
        if (ComponentState.READY == component.getState() ||
                ComponentState.STARTING == component.getState()) {
            return true;
        }

        return false;
    }

    private boolean firstPhaseReady() {
        for (ApplicationComponent component : firstPhaseComponents) {
            if (ComponentState.READY != component.getState()) {
                return false;
            }
        }

        return true;
    }

    private void updateAppState() {
        synchronized (this) {
            doUpdateAppState();
        }
    }

    private void doUpdateAppState() {
        blockingComponent = null;
        setState(ComponentState.READY);

        for (ApplicationComponent component : allComponents) {
            ComponentState componentState = component.getState();
            if (ComponentState.READY != componentState) {
                blockingComponent = component;

                if (ComponentState.ERROR == componentState) {
                    setState(ComponentState.ERROR);
                    return;
                } else {
                    setState(ComponentState.STARTING);
                }
            }
        }
    }

    @Override
    public ComponentState getState() {
        synchronized (this) {
            return super.getState();
        }
    }

    public ApplicationComponent getBlockingComponent() {
        return blockingComponent;
    }
}
