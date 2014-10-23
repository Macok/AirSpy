package com.mac.airspy;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Maciej on 2014-10-04.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private boolean stoppedByUser;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (stoppedByUser) {
            return;
        }

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (stoppedByUser) {
            return;
        }
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        tryStopPreview();

        handleDeviceRotation(w, h);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("", "Error starting camera preview: " + e.getMessage());
        }
    }

    private void handleDeviceRotation(int w, int h) {
        Display display = ((WindowManager)getContext()
                .getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Camera.Parameters parameters = mCamera.getParameters();

        Camera.Size size = null;
        int degrees = 0;

        if(display.getRotation() == Surface.ROTATION_0) {
            size = getBestPreviewSize(h, w, parameters);
            degrees = 90;
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            size = getBestPreviewSize(w, h, parameters);
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            size = getBestPreviewSize(h, w, parameters);
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            size = getBestPreviewSize(w, h, parameters);
            degrees = 180;
        }

        parameters.setPreviewSize(size.width, size.height);

        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(degrees);
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

    private void tryStopPreview() {
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
    }

    public void stop() {
        stoppedByUser = true;

        tryStopPreview();
        mCamera = null;
    }
}

