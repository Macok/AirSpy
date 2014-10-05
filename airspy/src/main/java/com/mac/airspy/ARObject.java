package com.mac.airspy;

import java.io.IOException;

/**
 * Created by Maciej on 02.02.14.
 */
public interface ARObject {
    public String getId();

    public String getName();

    public double getDistanceKm();

    public SimpleLocation getLocation();
}
