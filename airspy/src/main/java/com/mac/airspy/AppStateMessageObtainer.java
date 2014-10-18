package com.mac.airspy;

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Maciej on 2014-10-16.
 */

public class AppStateMessageObtainer {

    private final Context ctx;

    @Inject
    private ApplicationController applicationController;

    private final Properties properties = new Properties();

    @Inject
    public AppStateMessageObtainer(Context ctx) {
        this.ctx = ctx;
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.loading_messages);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e("", "", e);
        }
    }

    public String getCurrentStateMessage() {
        ComponentState state = applicationController.getState();

        String stateIdentifier;
        String stateMessage = null;
        if (ComponentState.READY != state) {
            ApplicationComponent blockingComponent = applicationController.getBlockingComponent();
            stateIdentifier = "message." + blockingComponent.getIdentifier() + "." + state.name();
            stateMessage = properties.getProperty(stateIdentifier);

            Log.d("", "blockingComponent: " + blockingComponent.getIdentifier());
        }

        if (stateMessage == null) {
            stateIdentifier = "message.default." + state.name();
            stateMessage = properties.getProperty(stateIdentifier);
        }

        return stateMessage;
    }

}
