package com.mac.airspy;

/**
 * Created by Maciej on 2014-10-03.
 */
public interface ApplicationComponent {
    public ComponentState getState();

    public void setStateListener(StateChangedListener stateListener);

    public static interface StateChangedListener {
        public void onStateChanged(ApplicationComponent component, ComponentState newState);
    }
}
