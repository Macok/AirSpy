package com.mac.airspy;

import android.graphics.PointF;

/**
 * Created by Maciej on 02.02.14.
 */
public class ObjectOnScreen {
    public final PointF position;
    public final ARObject object;

    public ObjectOnScreen(ARObject object, PointF position) {
        this.position = position;
        this.object = object;
    }
}
