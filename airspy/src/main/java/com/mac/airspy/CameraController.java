package com.mac.airspy;

import android.hardware.Camera;
import android.widget.FrameLayout;
import com.mac.airspy.parameters.CameraParameters;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2014-10-04.
 */
@ContextSingleton
public class CameraController extends BaseApplicationComponent {

    @InjectView(R.id.cameraPreview)
    private FrameLayout cameraView;

    private Camera camera;

    private CameraParameters cameraParameters;

    public void start() {
        camera = getCameraInstance();
        if (camera == null) {
            state = ComponentState.ERROR;
            return;
        }

        obtainCameraParameters();

        cameraView.removeAllViews();
        cameraView.addView(new CameraPreview(ctx, camera));

        state = ComponentState.READY;
    }

    private void obtainCameraParameters() {
        Camera.Parameters params = camera.getParameters();

        double cameraHorizontalAngle = Math.toRadians(params.getHorizontalViewAngle());
        double cameraVerticalAngle = Math.toRadians(params.getVerticalViewAngle());

        this.cameraParameters = new CameraParameters(cameraHorizontalAngle, cameraVerticalAngle);
    }

    public void pause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }

        state = ComponentState.STOPPED;
    }

    private Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e) {
        }
        return c;
    }

    public CameraParameters getCameraParameters() {
        return null;
    }
}
