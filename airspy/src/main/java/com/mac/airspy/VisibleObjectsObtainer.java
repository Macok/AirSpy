package com.mac.airspy;

import android.graphics.PointF;
import com.google.inject.Inject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-16.
 */
public class VisibleObjectsObtainer {
    @Inject
    private ScreenPositionCalculator screenPositionCalculator;

    @Inject
    private ObjectSourceManager objectSourceManager;

    public List<ObjectOnScreen> getObjectsOnScreen() {
        List<ObjectOnScreen> objectsOnScreen = new LinkedList<ObjectOnScreen>();
        List<ARObject> allObjects = objectSourceManager.getObjects();

        for (ARObject object : allObjects) {
            PointF screenPos = screenPositionCalculator.calculateScreenPosition(object.getLocation());
            if (screenPos == null)
                continue;

            objectsOnScreen.add(new ObjectOnScreen(object, screenPos));
        }

        return objectsOnScreen;
    }
}
