package com.mac.airspy;

import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import com.google.inject.Inject;
import com.mac.airspy.parameters.ScreenParameters;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Maciej on 2014-10-04.
 */

@ContextSingleton
public class ARLayer extends BaseApplicationComponent implements SurfaceHolder.Callback {

    @InjectView(R.id.arLayerView)
    private SurfaceView arLayerView;

    @InjectView(R.id.textView3)
    private TextView fpsView;

    @Inject
    private ObjectMarkersDrawer objectMarkersDrawer;

    @Inject
    private ARLayerTouchListener touchListener;

    @Inject
    private RadarDrawer radarDrawer;

    private SurfaceHolder holder;

    private ScreenParameters screenParameters;

    public void setFps(final int fps) {
        fpsView.post(new Runnable() {
            @Override
            public void run() {
                fpsView.setText("" + fps);
            }
        });
    }

    public void draw(List<ObjectOnScreen> objects) {
        Canvas canvas = null;

        try {
            canvas = holder.lockCanvas();

            doDraw(objects, canvas);
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void doDraw(List<ObjectOnScreen> objects, Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        objectMarkersDrawer.drawMarkers(canvas, objects);
        radarDrawer.draw(canvas);
    }

    public void init() {
        arLayerView.setZOrderMediaOverlay(true);

        holder = arLayerView.getHolder();
        holder.addCallback(this);

        arLayerView.setOnTouchListener(touchListener);
        holder.setFormat(PixelFormat.TRANSPARENT);

        radarDrawer.init();
    }

    public void release() {
        holder.removeCallback(this);
        setState(ComponentState.STOPPED);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width > 0 && height > 0) {
            screenParameters = new ScreenParameters(width, height);

            Log.d("Obtained screen params", "SizeX: " + screenParameters.sizeX
                    + " SizeY: " + screenParameters.sizeY);

            setState(ComponentState.READY);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        setState(ComponentState.STOPPED);
    }

    public ScreenParameters getScreenParameters() {
        return screenParameters;
    }
}
