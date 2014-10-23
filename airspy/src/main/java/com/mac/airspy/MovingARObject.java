package com.mac.airspy;

/**
 * Created by Maciej on 2014-10-23.
 */
public interface MovingARObject extends ARObject {
    public double getSpeedKmh();

    public double getTrack();

    public long getLastUpdateTime();
}
