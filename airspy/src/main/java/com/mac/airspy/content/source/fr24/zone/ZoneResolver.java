package com.mac.airspy.content.source.fr24.zone;

import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mac.airspy.content.source.fr24.FlightRadarClient;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.location.SimpleLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by Maciej on 09.01.14.
 */
public class ZoneResolver {
    private static final int ZONE_MARGIN_KM = 200;

    @Inject
    private FlightRadarClient client;

    @Inject
    private LocationService locationService;

    private Zone bestMatchingZone;

    public String getCurrentZone() throws IOException {
        JsonNode root;

        root = getZonesRootNode();

        SimpleLocation location = locationService.getLocation();

        bestMatchingZone = new Zone("full", null, null);

        getBestZone(root, location);

        return bestMatchingZone.getName();
    }

    private JsonNode getZonesRootNode() throws IOException {
        JsonNode root;
        InputStream istream = null;
        try {
            istream = client.getZonesStream();
            ObjectMapper om = new ObjectMapper();

            root = om.readTree(istream);
        } finally {
            if (istream != null) {
                istream.close();
            }
        }
        return root;
    }

    private void getBestZone(JsonNode root, SimpleLocation location) {
        Iterator<String> iter = root.fieldNames();

        while (iter.hasNext()) {
            String name = iter.next();
            JsonNode node = root.get(name);
            if (name.equals("version")) {
                continue;
            }

            Zone zone = Zone.fromNode(node);
            zone.setName(name);

            if (zone.contains(location, ZONE_MARGIN_KM)) {
                if (isBetterZone(zone)) {
                    bestMatchingZone = zone;
                }

                JsonNode next = node.get("subzones");
                if (next != null) {
                    getBestZone(next, location);
                }
            }
        }
    }

    private boolean isBetterZone(Zone zone) {
        return bestMatchingZone.getName().equals("full") ||
                zone.getArea() < bestMatchingZone.getArea();
    }

}
