package com.mac.airspy;

import android.content.Context;
import android.hardware.Camera;
import android.view.View;
import android.widget.FrameLayout;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2014-10-04.
 */
@ContextSingleton
public class CameraController implements ApplicationComponent {

    @Inject
    private Context ctx;

    @InjectView(R.id.cameraPreview)
    private FrameLayout cameraView;

    private ComponentState state = ComponentState.STOPPED;

    private Camera camera;

    public void start() {
        camera = getCameraInstance();
        if (camera == null) {
            state = ComponentState.ERROR;
            return;
        }

        cameraView.removeAllViews();
        cameraView.addView(new CameraPreview(ctx, camera));

        state = ComponentState.READY;
    }

    public void pause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }

        state = ComponentState.STOPPED;
    }

    public ComponentState getState() {
        return state;
    }

    private Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e) {
        }
        return c;
    }
}
