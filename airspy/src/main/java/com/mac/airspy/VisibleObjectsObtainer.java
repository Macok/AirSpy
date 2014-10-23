package com.mac.airspy;

import android.graphics.PointF;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-16.
 */

@ContextSingleton
public class VisibleObjectsObtainer {
    @Inject
    private ScreenPositionCalculator screenPositionCalculator;

    @Inject
    private ObjectsProvider objectsProvider;

    private List<ObjectOnScreen> objectsOnScreen;

    public void update() {
        List<ObjectOnScreen> objectsOnScreen = new LinkedList<ObjectOnScreen>();
        List<? extends ARObject> allObjects = objectsProvider.getObjects();

        for (ARObject object : allObjects) {
            PointF screenPos = screenPositionCalculator.calculateScreenPosition(object);
            if (screenPos == null)
                continue;

            objectsOnScreen.add(new ObjectOnScreen(object, screenPos));
        }

        this.objectsOnScreen = objectsOnScreen;
    }

    public List<ObjectOnScreen> getObjectsOnScreen() {
        return objectsOnScreen;
    }
}
