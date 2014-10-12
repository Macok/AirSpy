package com.mac.airspy;

import android.content.Context;
import com.google.inject.Inject;

/**
 * Created by Maciej on 2014-10-04.
 */
public class BaseApplicationComponent implements ApplicationComponent {
    @Inject
    protected Context ctx;

    protected ComponentState state = ComponentState.STOPPED;

    private StateChangedListener stateListener;

    protected void setState(ComponentState state) {
        if (this.state != state) {
            this.state = state;

            if (stateListener != null) {
                stateListener.onStateChanged(this, state);
            }
        }
    }

    @Override
    public void setStateListener(StateChangedListener stateListener) {
        this.stateListener = stateListener;
    }

    @Override
    public ComponentState getState() {
        return state;
    }
}
