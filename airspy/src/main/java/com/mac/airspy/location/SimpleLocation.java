package com.mac.airspy.location;

import android.location.Location;

/**
 * Created by Maciej on 09.01.14.
 */
public class SimpleLocation {
    private double longtitude;
    private double latitude;
    private double altitude;

    public SimpleLocation(double longtitude, double latitude, double altitude) {
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public SimpleLocation(Location location) {
        this(location.getLongitude(), location.getLatitude(), location.getAltitude() / 1000);
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
