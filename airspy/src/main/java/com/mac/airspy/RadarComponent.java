package com.mac.airspy;

import android.content.Context;
import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.utils.ResolutionUtils;
import com.mac.airspy.utils.Vector3D;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2015-03-22.
 */

@ContextSingleton
public class RadarComponent {
    private static final float RADAR_SCALE_RATIO = 0.85f;
    public static final int OBJECT_CIRCLE_RADIUS_DP = 3;
    public static final int RADAR_MARGIN_DP = 15;
    public static final int RADAR_SIZE_DP = 150;

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

    private float width;
    private float height;

    public RadarComponent() {
        planePaint = new Paint();
        planePaint.setColor(Color.RED);

        radarBgPaint = new Paint();
        radarBgPaint.setAlpha(180);
    }

    public void init() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        radarBackground = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.radar_bg, options);
        radarForeground = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.radar_fg, options);

        width = ResolutionUtils.dpToPx(ctx, RADAR_SIZE_DP);
        height = ResolutionUtils.dpToPx(ctx, RADAR_SIZE_DP);

        scaleBitmaps();
    }

    public void draw(Canvas canvas) {
        float bearing = orientationService.getOrientation()[0];

        canvas.save();

        float radarMargin = ResolutionUtils.dpToPx(ctx, RADAR_MARGIN_DP);
        canvas.translate(radarMargin, radarMargin);

        canvas.save();
        //canvas.translate();
        canvas.rotate((float) -Math.toDegrees(bearing), width / 2, height / 2);
        canvas.drawBitmap(radarBackgroundScaled, 0, 0, radarBgPaint);

        drawObjects(canvas);

        canvas.restore();

        canvas.drawBitmap(radarForegroundScaled, 0, 0, null);

        canvas.restore();
    }

    private void drawObjects(Canvas canvas) {
        for (ARObject object : objectsProvider.getObjectsInRange()) {
            Vector3D distVector = object.getApproximatedDistanceVector();

            float dx = (float) ((distVector.getX() / preferencesHelper.getRange()) * width / 2)
                    * RADAR_SCALE_RATIO;
            float dy = -(float) ((distVector.getY() / preferencesHelper.getRange()) * height / 2)
                    * RADAR_SCALE_RATIO;

            float objectCircleRadius = ResolutionUtils.dpToPx(ctx, OBJECT_CIRCLE_RADIUS_DP);
            canvas.drawCircle(width / 2 + dx, height / 2 + dy, objectCircleRadius, planePaint);
        }

    }

    private void scaleBitmaps() {
        radarBackgroundScaled = Bitmap.createScaledBitmap(radarBackground, (int) width, (int) height, true);
        radarForegroundScaled = Bitmap.createScaledBitmap(radarForeground, (int) width, (int) height, true);
    }
}
