package com.mac.airspy;

import com.mac.airspy.location.SimpleLocation;

/**
 * Created by Maciej on 02.02.14.
 */
public interface ARObject {
    public String getId();

    public String getName();

    public double getDistanceKm();

    public SimpleLocation getLocation();
}
