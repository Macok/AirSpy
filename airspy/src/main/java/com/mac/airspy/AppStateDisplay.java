package com.mac.airspy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

/**
 * Created by Maciej on 2014-10-16.
 */

@ContextSingleton
public class AppStateDisplay implements ApplicationComponent.StateChangedListener {

    private final Activity activity;

    private final ApplicationController applicationController;

    @Inject
    private AppStateMessageObtainer stateMessageObtainer;

    private ProgressDialog loadingDialog;
    private AlertDialog errorDialog;

    private boolean dismissed;

    @Inject
    public AppStateDisplay(Context activity, ApplicationController applicationController) {
        this.activity = (Activity) activity;
        this.applicationController = applicationController;

        applicationController.setStateListener(this);

        createLoadingDialog();

        createErrorDialog();
    }

    @Override
    public void onStateChanged(ApplicationComponent component, ComponentState newState) {
        if (dismissed) {
            return;
        }

        String message = stateMessageObtainer.getCurrentStateMessage();

        switch (newState) {
            case READY:
                loadingDialog.hide();
                errorDialog.hide();
                break;
            case ERROR:
                loadingDialog.hide();
                errorDialog.show();
                errorDialog.setMessage(message);
                break;
            default:
                errorDialog.hide();
                loadingDialog.show();
                loadingDialog.setMessage(message);
        }
    }

    public void dismiss() {
        dismissed = true;

        loadingDialog.dismiss();
        errorDialog.dismiss();
    }

    private void createErrorDialog() {
        errorDialog = new AlertDialog.Builder(activity)
                .setTitle("Error")
                .setMessage("")
                .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishActivity();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();

        errorDialog.setCanceledOnTouchOutside(false);
        errorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finishActivity();
            }
        });
    }

    private void createLoadingDialog() {
        loadingDialog = new ProgressDialog(activity);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finishActivity();
            }
        });

    }

    private void finishActivity() {
        activity.finish();
    }
}
