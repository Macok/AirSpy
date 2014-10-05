package com.mac.airspy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.inject.Inject;
import com.mac.airspy.parameters.ScreenParameters;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Created by Maciej on 2014-10-04.
 */
public class ARLayer extends BaseApplicationComponent implements SurfaceHolder.Callback {

    @InjectView(R.id.arLayerView)
    private SurfaceView arLayerView;

    private SurfaceHolder holder;

    private ScreenParameters screenParameters;

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
            canvas.drawCircle(0, 0, 15, paint);
            canvas.restore();
        }
    }

    public void init() {
        holder = arLayerView.getHolder();
        holder.addCallback(this);

        if (holder.isCreating()) {
            setState(ComponentState.STARTING);
        }else {
            setState(ComponentState.READY);
        }
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
        screenParameters = new ScreenParameters(width, height);
        setState(ComponentState.READY);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        setState(ComponentState.STOPPED);
    }

    public ScreenParameters getScreenParameters() {
        return screenParameters;
    }
}
