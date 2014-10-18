package com.mac.airspy.content.source.fr24;

import com.mac.airspy.ARObject;
import com.mac.airspy.location.SimpleLocation;

/**
 * Created by Maciej on 2014-10-18.
 */
public class Plane implements ARObject{

    private final String hex;
    private SimpleLocation location;
    private Double distanceKm;

    public Plane(String hex, SimpleLocation location, Double distanceKm) {
        this.hex = hex;
        this.location = location;
        this.distanceKm = distanceKm;
    }

    @Override
    public String getId() {
        return hex;
    }

    @Override
    public String getName() {
        return hex;
    }

    @Override
    public double getDistanceKm() {
        return distanceKm;
    }

    @Override
    public SimpleLocation getLocation() {
        return location;
    }
}
