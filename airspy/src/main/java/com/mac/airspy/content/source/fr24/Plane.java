package com.mac.airspy.content.source.fr24;

import com.mac.airspy.ARObject;
import com.mac.airspy.location.SimpleLocation;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Maciej on 2014-10-18.
 */
public class Plane implements ARObject{

    private String hex;
    private SimpleLocation location;
    private Double distanceKm;
    private String callsign;
    private String flightNumber;
    private String aircraftCode;
    private String registration;
    private String fromCode;
    private String toCode;
    private double speedKmh;

    public Plane() {
    }

    @Override
    public String getId() {
        return hex;
    }

    @Override
    public String getName() {
        if (!StringUtils.isBlank(callsign))
            return callsign;

        if (!StringUtils.isBlank(flightNumber))
            return flightNumber;

        return "UNKNOWN";
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

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRegistration() {
        return registration;
    }

    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }

    public String getFromCode() {
        return fromCode;
    }

    public void setToCode(String toCode) {
        this.toCode = toCode;
    }

    public String getToCode() {
        return toCode;
    }

    public void setSpeedKmh(double speedKmh) {
        this.speedKmh = speedKmh;
    }

    public double getSpeedKmh() {
        return speedKmh;
    }
}
