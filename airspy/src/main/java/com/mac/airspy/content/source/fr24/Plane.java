package com.mac.airspy.content.source.fr24;

import com.mac.airspy.ARObject;
import com.mac.airspy.location.SimpleLocation;

/**
 * Created by Maciej on 2014-10-18.
 */
public class Plane implements ARObject{

    private String hex;
    private SimpleLocation location;
    private Double distanceKm;
    private String callsign;

    public Plane() {
    }

    @Override
    public String getId() {
        return hex;
    }

    @Override
    public String getName() {
        return hex;
    }

    public void setLocation(SimpleLocation location) {
        this.location = location;
    }

    @Override
    public double getDistanceKm() {
        return distanceKm;
    }

    @Override
    public SimpleLocation getLocation() {
        return location;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCallsign() {
        return callsign;
    }
}
