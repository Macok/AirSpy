package com.mac.airspy.content.source.fr24;

import com.mac.airspy.BaseARObject;
import com.mac.airspy.MovingARObject;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.Vector3D;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Maciej on 2014-10-18.
 */
public class Plane extends BaseARObject implements MovingARObject{

    private String hex;
    private String callsign;
    private String flightNumber;
    private String aircraftCode;
    private String registration;
    private String fromCode;
    private String toCode;
    private double speedKmh;
    private double track;
    private long dataTimestamp;

    public Plane(String id) {
        super(id);
    }

    @Override
    public String getName() {
        if (!StringUtils.isBlank(callsign))
            return callsign;

        if (!StringUtils.isBlank(flightNumber))
            return flightNumber;

        return "UNKNOWN";
    }

    @Override
    public double getSpeedKmh() {
        return speedKmh;
    }

    @Override
    public double getTrack() {
        return track;
    }

    @Override
    public long getLastUpdateTime() {
        return dataTimestamp;
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

    public void setTrack(double track) {
        this.track = track;
    }

    public void setDataTimestamp(long dataTimestamp) {
        this.dataTimestamp = dataTimestamp;
    }

    public long getDataTimestamp() {
        return dataTimestamp;
    }
}
