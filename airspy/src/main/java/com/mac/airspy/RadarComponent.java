package com.mac.airspy;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectResource;
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

    @Inject
    private UserPreferencesHelper preferencesHelper;

    @Inject
    private Context ctx;

    private Bitmap radarBackground;
    private Bitmap radarForeground;

    private Bitmap radarBackgroundScaled;
    private Bitmap radarForegroundScaled;

    private Paint planePaint;
    private Paint radarBgPaint;

    private boolean ready;
    private boolean radarVisible;
    private int width;
    private int height;

    public RadarComponent() {
        planePaint = new Paint();
        planePaint.setColor(Color.RED);

        radarBgPaint = new Paint();
        radarBgPaint.setAlpha(150);
    }

    public void init() {
        surfaceView.getHolder().addCallback(this);
        surfaceView.setZOrderOnTop(true);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        radarBackground = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.radar_bg, options);
        radarForeground = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.radar_fg, options);

        radarVisible = preferencesHelper.isRadarVisible();

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarVisible = !preferencesHelper.isRadarVisible();
                preferencesHelper.setRadarVisible(radarVisible);
            }
        });
    }

    public void draw() {
        if (ready) {
            SurfaceHolder holder = surfaceView.getHolder();
            holder.setFormat(PixelFormat.TRANSPARENT);

            Canvas canvas = holder.lockCanvas();
            try {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                if (radarVisible) {
                    float bearing = orientationService.getOrientation()[0];

                    canvas.save();

                    canvas.rotate((float) -Math.toDegrees(bearing), width / 2, height / 2);
                    canvas.drawBitmap(radarBackgroundScaled, 0, 0, radarBgPaint);
                    canvas.drawCircle(width / 2, height / 5, 20, planePaint);

                    canvas.restore();

                    canvas.drawBitmap(radarForegroundScaled, 0, 0, null);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
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

            scaleBitmaps();

            ready = true;
        }
    }

    private void scaleBitmaps() {
        radarBackgroundScaled = Bitmap.createScaledBitmap(radarBackground, width, height, true);
        radarForegroundScaled = Bitmap.createScaledBitmap(radarForeground, width, height, true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ready = false;
    }
}
