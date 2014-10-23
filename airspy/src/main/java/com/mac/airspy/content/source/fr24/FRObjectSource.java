package com.mac.airspy.content.source.fr24;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.content.ObjectDetailsViewProvider;
import com.mac.airspy.content.ObjectSource;
import com.mac.airspy.content.source.fr24.zone.ZoneResolver;

import java.io.IOException;
import java.util.List;

/**
 * Created by Maciej on 2014-10-18.
 */
public class FRObjectSource implements ObjectSource {

    @Inject
    private ZoneResolver zoneResolver;

    @Inject
    private TrafficProcessor trafficProcessor;

    @Inject
    private FRDetailsViewProvider detailsProvider;

    private String currentZone;

    @Override
    public List<? extends ARObject> getObjects() throws IOException {

        if (currentZone == null) {
            currentZone = zoneResolver.getCurrentZone();
        }
        Log.d("ZONE", currentZone);


        return trafficProcessor.getPlanes(100, currentZone);
    }

    @Override
    public ObjectDetailsViewProvider getDetailsProvider() {
        return detailsProvider;
    }
}
