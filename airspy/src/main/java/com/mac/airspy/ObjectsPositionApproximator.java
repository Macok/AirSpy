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
    @Inject
    private ObjectsProvider objectsProvider;

    @Inject
    private LocationService locationService;

    public void approximate() {
        for (ARObject object : objectsProvider.getAllObjects()) {
            SimpleLocation userLocation = locationService.getLocation();
            Vector3D distanceVector = MathUtils.calculateApproximatedDistanceVector(userLocation, object);
            object.setApproximatedDistanceVector(distanceVector);
        }

    }
}
