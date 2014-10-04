package com.mac.airspy;

/**
 * Created by Maciej on 2014-10-03.
 */
public interface ApplicationComponent {
    public void start();

    public void stop();

    public ComponentState getState();
}
