package com.mac.airspy;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Camera;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.google.inject.Inject;
import com.mac.airspy.parameters.CameraParameters;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2014-10-04.
 */
@ContextSingleton
public class CameraController extends BaseApplicationComponent {

    @InjectView(R.id.cameraPreview)
    private FrameLayout cameraPreviewContainer;

    @Inject
    private Resources resources;

    private Camera camera;

    private CameraParameters cameraParameters;
    private CameraPreview cameraPreview;

    public void resume() {
        camera = getCameraInstance();
        if (camera == null) {
            setState(ComponentState.ERROR);
            return;
        }

        obtainCameraParameters();

        cameraPreview = new CameraPreview(ctx, camera);

        cameraPreviewContainer.removeAllViews();
        cameraPreviewContainer.addView(cameraPreview);

        setState(ComponentState.READY);
    }

    private void obtainCameraParameters() {
        Camera.Parameters params = camera.getParameters();

        double cameraHorizontalAngle = Math.toRadians(params.getHorizontalViewAngle());
        double cameraVerticalAngle = Math.toRadians(params.getVerticalViewAngle());

        if (Configuration.ORIENTATION_LANDSCAPE == resources.getConfiguration().orientation) {
            this.cameraParameters = new CameraParameters(cameraHorizontalAngle, cameraVerticalAngle);
        }else {
            this.cameraParameters = new CameraParameters(cameraVerticalAngle, cameraHorizontalAngle);
        }

        Log.d("Obtained camera parameters",
                "Horizontal angle: " + cameraParameters.horizontalViewAngle +
                " Vertical angle: " + cameraParameters.verticalViewAngle);
    }

    public void pause() {
        if (camera != null) {
            cameraPreview.stop();
            camera.release();
            camera = null;
        }

        setState(ComponentState.STOPPED);
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
        return cameraParameters;
    }
}
