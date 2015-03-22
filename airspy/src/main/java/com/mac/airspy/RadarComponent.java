package com.mac.airspy;

import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2015-03-22.
 */

@ContextSingleton
public class RadarComponent implements SurfaceHolder.Callback {
    @InjectView(R.id.radarView)
    private SurfaceView surfaceView;

    @Inject
    private ObjectsProvider objectsProvider;

    @Inject
    private OrientationService orientationService;

    private boolean ready;
    private int width;
    private int height;

    public void init() {
        surfaceView.getHolder().addCallback(this);
        surfaceView.setZOrderOnTop(true);
    }

    public void draw() {
        if (ready) {
            SurfaceHolder holder = surfaceView.getHolder();
            holder.setFormat(PixelFormat.TRANSPARENT);

            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            Paint paint = new Paint();
            paint.setColor(Color.RED);


            float bearing = orientationService.getOrientation()[0];
            canvas.rotate((float) -Math.toDegrees(bearing), width / 2, height / 2);
            canvas.drawCircle(width / 2, height / 5, 20, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width > 0 && height > 0) {
            this.width = width;
            this.height = height;
            ready = true;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ready = false;
    }
}
