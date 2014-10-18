package com.mac.airspy.content.source.test;

import com.mac.airspy.ARObject;
import com.mac.airspy.location.SimpleLocation;

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
    public double getDistanceKm() {
        return 0;
    }

    @Override
    public SimpleLocation getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
