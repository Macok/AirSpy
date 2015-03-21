package com.mac.airspy.content.source.fr24;

import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.MathUtils;
import com.mac.airspy.utils.Vector3D;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maciej on 03.03.14.
 */
public class TrafficProcessor {
    private static final String TAG = TrafficProcessor.class.getSimpleName();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private FlightRadarClient frClient;

    @Inject
    private LocationService locationHolder;

    public List<Plane> getPlanes(int range, Double[] bounds) throws IOException {
        InputStream trafficStream = frClient.getTrafficStream(bounds);

        try {
            JsonNode trafficNode = objectMapper.readTree(trafficStream).get("aircraft");

            List<Plane> objects = new LinkedList<>();
            for (JsonNode aircraftNode : trafficNode) {
                Plane plane = processPlane(aircraftNode, range);
                if (plane != null) {
                    objects.add(plane);
                }
            }

            Log.d(TAG, "Zone: " + bounds + ", range: " + range + ", found: " + objects.size() + " planes");

            return objects;
        } finally {
            trafficStream.close();
        }
    }

    private Plane processPlane(JsonNode node, int range) throws IOException{

        String id = node.get(0).asText();
        String hex = node.get(1).asText();

        double lat = node.get(2).asDouble();
        double lon = node.get(3).asDouble();

        double altitudeFeet = node.get(5).asDouble();
        double altitudeMeters = MathUtils.feetToMeters(altitudeFeet);
        if (altitudeMeters < 20) {
            //TODO return null;
        }

        double speedKt = node.get(6).asDouble();
        double speedKmh = MathUtils.knotsToKmh(speedKt);

        if (speedKmh < 30) {
            //TODO return null;
        }

        double track = node.get(4).asDouble();
        String aircraft = node.get(9).asText();
        String registration = node.get(10).asText();
        long timestampSeconds = node.get(11).asLong();
        String from = node.get(12).asText();
        String to = node.get(13).asText();
        String flightNumber = node.get(14).asText();
        String callsign = node.get(17).asText();


        SimpleLocation planeLocation = new SimpleLocation(lon, lat, altitudeMeters / 1000);

        SimpleLocation userLocation = locationHolder.getLocation();
        Vector3D distVector = MathUtils.calculateDistanceVector(planeLocation, userLocation);

        double distance = distVector.length();
        if (distance > range)
            return null;

        Plane plane = new Plane(id);
        plane.setHex(hex);
        plane.setFlightNumber(flightNumber);
        plane.setCallsign(callsign);
        plane.setLocation(planeLocation);
        plane.setDistanceKm(distance);
        plane.setAircraftCode(aircraft);
        plane.setRegistration(registration);
        plane.setFromCode(from);
        plane.setToCode(to);
        plane.setSpeedKmh(speedKmh);
        plane.setTrack(track);
        plane.setDataTimestamp(timestampSeconds * 1000);

        return plane;
    }
}
