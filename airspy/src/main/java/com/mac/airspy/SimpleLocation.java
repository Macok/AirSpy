package com.mac.airspy;

import android.location.Location;

/**
 * Created by Maciej on 09.01.14.
 */
public class SimpleLocation {
    private double longtitude;
    private double latitude;
    private double altitude;

    public SimpleLocation(double longtitude, double latitude, double altitudeMeters) {
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.altitude = altitudeMeters / 1000;
    }

    public SimpleLocation(Location location) {
        this(location.getLongitude(), location.getLatitude(), location.getAltitude());
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }
}
