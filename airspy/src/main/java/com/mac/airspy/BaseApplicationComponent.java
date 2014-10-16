package com.mac.airspy;

import android.content.Context;
import android.os.Looper;
import com.google.inject.Inject;

/**
 * Created by Maciej on 2014-10-04.
 */
public abstract class BaseApplicationComponent implements ApplicationComponent {
    @Inject
    protected Context ctx;

    protected ComponentState state = ComponentState.STOPPED;

    private StateChangedListener stateListener;

    protected void setState(ComponentState state) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("setState called outside UI thread");
        }

        if (this.state != state) {
            doSetState(state);
        }
    }

    protected void doSetState(ComponentState state) {

        this.state = state;

        if (stateListener != null) {
            stateListener.onStateChanged(this, state);
        }
    }

    @Override
    public String getIdentifier() {
        return this.getClass().getSimpleName();
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
