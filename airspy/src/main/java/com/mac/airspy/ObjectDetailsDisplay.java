package com.mac.airspy;

import roboguice.inject.ContextSingleton;

/**
 * Created by Maciej on 2014-10-21.
 */

@ContextSingleton
public class ObjectDetailsDisplay {

    private ARObject currentObject;

    public void showObjectDetails(ARObject object) {
        currentObject = object;
    }

    public ARObject getCurrentObject() {
        return currentObject;
    }
}
