package com.mac.airspy;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.mac.airspy.parameters.ScreenParameters;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

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
        Canvas canvas = holder.lockCanvas();

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawObjects(canvas, objects);

        holder.unlockCanvasAndPost(canvas);
    }

    private void drawObjects(Canvas canvas, List<ObjectOnScreen> objects) {
        for (ObjectOnScreen object : objects) {
            canvas.save();
            canvas.translate(object.position.x, object.position.y);
            Paint paint = new Paint();
            paint.setColor(0xffff0000);
            canvas.drawCircle(0, 0, 45, paint);
            canvas.restore();
        }
    }

    public void init() {
        holder = arLayerView.getHolder();
        holder.addCallback(this);

        arLayerView.setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
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
