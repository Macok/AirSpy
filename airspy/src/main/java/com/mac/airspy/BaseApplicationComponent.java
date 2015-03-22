package com.mac.airspy;

import android.app.Activity;
import android.content.Context;
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
        if (this.state != state) {
            BaseApplicationComponent.this.state = state;
            notifyListener();
        }
    }

    protected void notifyListener() {
        Activity activity = (Activity) ctx;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stateListener != null) {
                    stateListener.onStateChanged(BaseApplicationComponent.this, state);
                }
            }
        });
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
