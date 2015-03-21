package com.mac.airspy;

import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;
import roboguice.inject.ContextSingleton;

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
    private final ObjectsProvider objectsProvider;

    private ApplicationComponent[] allComponents;
    private ApplicationComponent[] firstPhaseComponents;

    //component that currently makes app state ERROR, STARTING or STOPPED
    private ApplicationComponent blockingComponent;

    private boolean starting;

    @Inject
    public ApplicationController(
            LocationService locationService,
            OrientationService orientationService,
            CameraController cameraController,
            ARLayer arLayer,
            MainLoopController mainLoopController,
            ObjectsProvider objectsProvider
    ) {
        this.locationService = locationService;
        this.orientationService = orientationService;
        this.cameraController = cameraController;
        this.arLayer = arLayer;
        this.mainLoopController = mainLoopController;
        this.objectsProvider = objectsProvider;

        initComponentsLists();

        for (ApplicationComponent component : allComponents) {
            component.setStateListener(this);
        }
    }

    private void initComponentsLists() {
        allComponents = new ApplicationComponent[]{
                objectsProvider,
                mainLoopController,
                locationService,
                arLayer,
                orientationService,
                cameraController
        };

        firstPhaseComponents = new ApplicationComponent[]{
                locationService,
                orientationService,
                cameraController,
                arLayer
        };
    }

    public void create() {
        starting = true;

        arLayer.init();
    }

    public void start() {
        starting = true;

        locationService.start();
        orientationService.start();
    }

    public void resume() {
        starting = true;

        cameraController.resume();
    }

    public void pause() {
        starting = false;

        cameraController.pause();

        if (ComponentState.STOPPED != mainLoopController.getState()) {
            mainLoopController.stop();
        }

        if (ComponentState.STOPPED != objectsProvider.getState()) {
            objectsProvider.stop();
        }
    }

    public void stop() {
        starting = false;

        orientationService.stop();
        locationService.stop();
    }

    public void destroy() {
        starting = false;

        arLayer.release();
    }

    @Override
    public void onStateChanged(ApplicationComponent component, ComponentState newState) {
        updateAppState();

        if (firstPhaseReady() && starting) {
            if (ComponentState.STOPPED == mainLoopController.getState()) {
                mainLoopController.start();
            }

            if (ComponentState.STOPPED == objectsProvider.getState()) {
                objectsProvider.start();
            }
        }
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
        ComponentState newState = ComponentState.READY;

        for (ApplicationComponent component : allComponents) {
            ComponentState componentState = component.getState();
            if (ComponentState.READY != componentState) {
                blockingComponent = component;

                if (ComponentState.ERROR == componentState) {
                    newState = ComponentState.ERROR;
                    break;
                } else {
                    newState = starting ? ComponentState.STARTING : ComponentState.STOPPED;
                }
            }
        }

        setState(newState);
    }


    @Override
    protected void setState(ComponentState state) {
        //notify listener even if new state is the same as old
        super.doSetState(state);
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
