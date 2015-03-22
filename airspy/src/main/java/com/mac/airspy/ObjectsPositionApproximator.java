package com.mac.airspy;

import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.MathUtils;
import com.mac.airspy.utils.Vector3D;

/**
 * Created by Maciej on 2015-03-22.
 */
public class ObjectsPositionApproximator {
    private static final long APPROXIMATION_INTERVAL_MILLIS = 1000;

    @Inject
    private ObjectsProvider objectsProvider;

    @Inject
    private LocationService locationService;

    private long lastApproximationTime = 0;

    public void updateApproximatedPositions() {
        long time = System.currentTimeMillis();
        if (time - lastApproximationTime > APPROXIMATION_INTERVAL_MILLIS) {
            lastApproximationTime = time;

            doUpdateApproximatedPositions();
        }
    }

    private void doUpdateApproximatedPositions() {
        for (ARObject object : objectsProvider.getAllObjects()) {
            SimpleLocation userLocation = locationService.getLocation();
            Vector3D distanceVector = MathUtils.calculateApproximatedDistanceVector(userLocation, object);
            object.setApproximatedDistanceVector(distanceVector);
        }
    }
}
