package com.mac.airspy;

import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.Vector3D;

/**
 * Created by Maciej on 2015-04-04.
 */
public abstract class BaseARObject implements ARObject {
    protected final String id;
    protected SimpleLocation location;
    protected Vector3D approximatedDistanceVector;

    public BaseARObject(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public abstract String getName();

    @Override
    public Vector3D getApproximatedDistanceVector() {
        return approximatedDistanceVector;
    }

    @Override
    public void setApproximatedDistanceVector(Vector3D approximatedDistanceVector) {
        this.approximatedDistanceVector = approximatedDistanceVector;
    }

    @Override
    public SimpleLocation getLocation() {
        return location;
    }

    public void setLocation(SimpleLocation location) {
        this.location = location;
    }
}
