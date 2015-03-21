package com.mac.airspy.content.source.fr24;

import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.location.SimpleLocation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Maciej on 2015-03-21.
 */
public class ZoneBoundsResolver {

    public static final int LONGTITUDE_MARGIN = 3;
    public static final int LATITUDE_MARGIN = 3;
    @Inject
    private LocationService locationService;

    public Double[] getBounds() {
        SimpleLocation location = locationService.getLocation();
        double latitude = location.getLatitude();
        double longtitude = location.getLongtitude();

        return new Double[]{
                latitude + LATITUDE_MARGIN,
                latitude - LATITUDE_MARGIN,
                longtitude - LONGTITUDE_MARGIN,
                longtitude + LONGTITUDE_MARGIN
        };
    }



}
