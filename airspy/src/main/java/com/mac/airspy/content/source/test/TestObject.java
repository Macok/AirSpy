package com.mac.airspy.content.source.test;

import com.mac.airspy.ARObject;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.Vector3D;

/**
 * Created by Maciej on 04.02.14.
 */
public class TestObject implements ARObject {

    private String name;

    private SimpleLocation location;

    public TestObject(String name, SimpleLocation location) {
        this.name=name;
        this.location = location;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public SimpleLocation getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public Vector3D getApproximatedDistanceVector() {
        return null;
    }

    @Override
    public void setApproximatedDistanceVector(Vector3D approximatedDistanceVector) {

    }
}
