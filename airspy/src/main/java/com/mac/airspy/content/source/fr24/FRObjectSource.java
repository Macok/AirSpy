package com.mac.airspy.content.source.fr24;

import android.util.Log;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.content.ObjectSource;
import com.mac.airspy.content.source.fr24.zone.ZoneResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-18.
 */
public class FRObjectSource implements ObjectSource {

    @Inject
    private ZoneResolver zoneResolver;

    @Override
    public List<ARObject> getObjects() {

        try {
            String currentZone = zoneResolver.getCurrentZone();
            Log.d("ZONE", currentZone);
        } catch (IOException e) {
            Log.e("", "", e);
        }

        return new ArrayList<>();
    }
}
