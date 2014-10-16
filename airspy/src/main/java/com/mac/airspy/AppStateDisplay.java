package com.mac.airspy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

/**
 * Created by Maciej on 2014-10-16.
 */

@ContextSingleton
public class AppStateDisplay implements ApplicationComponent.StateChangedListener {

    private final MainActivity activity;

    private final ApplicationController applicationController;

    @Inject
    private AppStateMessageObtainer stateMessageObtainer;

    private final ProgressDialog dialog;

    @Inject
    public AppStateDisplay(Context activity, ApplicationController applicationController) {
        this.activity = (MainActivity) activity;
        this.applicationController = applicationController;

        dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AppStateDisplay.this.activity.finish();
            }
        });

        dialog.show();

        applicationController.setStateListener(this);
    }

    @Override
    public void onStateChanged(ApplicationComponent component, ComponentState newState) {
        ApplicationComponent blockingComponent = applicationController.getBlockingComponent();
        Log.d("", "APPSTATE: " + newState);
        if (blockingComponent != null) {
            Log.d("", "APPSTATE: " + " BLOCKING: " + blockingComponent.getIdentifier() +
                    " BLOCKING STATE: " +  blockingComponent.getState());
        }

        String message = stateMessageObtainer.getCurrentStateMessage();

        if (newState == ComponentState.READY) {
            dialog.dismiss();
        } else {
            dialog.show();
        }

        dialog.setMessage(message);
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
