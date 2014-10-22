package com.mac.airspy;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.google.inject.Inject;
import com.mac.airspy.utils.ResolutionUtils;

import java.util.List;

/**
 * Created by Maciej on 2014-10-21.
 */
public class ARLayerTouchListener implements View.OnTouchListener {

    @Inject
    private VisibleObjectsObtainer visibleObjectsObtainer;

    @Inject
    private ObjectDetailsDisplay objectDetailsDisplay;

    private static final int OBJECT_SELECTION_RADIUS_DP = 50;
    private static final int OBJECT_MARKER_HEIGHT_DP = 60;
    private final float OBJECT_SELECTION_RADIUS_PX;
    private final float OBJECT_MARKER_HEIGHT_PX;

    @Inject
    public ARLayerTouchListener(Context ctx) {
        OBJECT_SELECTION_RADIUS_PX = ResolutionUtils.dpToPx(ctx, OBJECT_SELECTION_RADIUS_DP);
        OBJECT_MARKER_HEIGHT_PX = ResolutionUtils.dpToPx(ctx, OBJECT_MARKER_HEIGHT_DP);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //object marker is drawn above object's coordinates
            event.setLocation(event.getX(),
                    event.getY() + OBJECT_MARKER_HEIGHT_PX / 2);

            ObjectOnScreen nearestObject = getNearestObject(event);
            if (nearestObject != null) {
                if (calculateDistance(nearestObject, event) < OBJECT_SELECTION_RADIUS_PX) {
                    objectDetailsDisplay.showObjectInfo(nearestObject.object);
                    return false;
                }
            }

            objectDetailsDisplay.hide();
        }

        return false;
    }

    private ObjectOnScreen getNearestObject(MotionEvent event) {
        List<ObjectOnScreen> objectsOnScreen = visibleObjectsObtainer.getObjectsOnScreen();
        ObjectOnScreen bestMatch = null;

        if (objectsOnScreen != null && !objectsOnScreen.isEmpty()) {
            double smallestDistance = 0;

            for (ObjectOnScreen object : objectsOnScreen) {
                double distance = calculateDistance(object, event);

                if (bestMatch == null || distance < smallestDistance) {
                    bestMatch = object;
                    smallestDistance = distance;
                }
            }
        }

        return bestMatch;
    }

    private static double calculateDistance(ObjectOnScreen object, MotionEvent event) {
        float dx = object.position.x - event.getX();
        float dy = object.position.y - event.getY();

        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
