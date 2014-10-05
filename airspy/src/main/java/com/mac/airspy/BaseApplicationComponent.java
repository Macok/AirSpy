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

    @Override
    public ComponentState getState() {
        return state;
    }
}
