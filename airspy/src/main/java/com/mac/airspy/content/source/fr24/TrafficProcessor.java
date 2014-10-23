package com.mac.airspy.content.source.fr24;

import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
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

    @Inject
    private FlightRadarClient frClient;

    @Inject
    private LocationService locationHolder;

    public List<Plane> getPlanes(int range, String zone) throws IOException {
        InputStream trafficStream = frClient.getTrafficStream(zone);

        JsonFactory factory = new JsonFactory();
        JsonParser jp = factory.createParser(trafficStream);

        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new IOException("JsonToken.START_OBJECT excepted");
        }

        List<Plane> objects = new LinkedList<>();
        
        while (jp.nextToken() == JsonToken.FIELD_NAME) {
            String fieldname = jp.getText();

            if (fieldname.equals("full_count") || fieldname.equals("version")) {
                jp.nextToken();
                continue;
            }

            Plane plane = processPlane(jp, range);
            if(plane !=null) {
                objects.add(plane);
            }
        }

        Log.d(TAG, "Zone: " + zone + ", range: " + range + ", found: " + objects.size() + " planes");

        return objects;
    }

    private Plane processPlane(JsonParser jp, int range) throws IOException{
        jp.nextToken(); //start array

        jp.nextToken(); //hex

        String hex = jp.getValueAsString();

        jp.nextToken(); //latitude

        double lat = jp.getValueAsDouble();

        jp.nextToken(); //longtitude

        double lon = jp.getValueAsDouble();

        jp.nextToken(); //track
        double track = jp.getValueAsDouble();

        jp.nextToken(); //altitude

        double altitudeFeet = jp.getValueAsDouble();
        if (altitudeFeet < 20) {
            //TODO return null;
        }

        jp.nextToken(); //speed kt

        double speedKt = jp.getValueAsDouble();
        double speedKmh = MathUtils.knotsToKmh(speedKt);

        jp.nextToken(); //sqawk
        jp.nextToken(); //radar
        jp.nextToken(); //aircraft

        String aircraft = jp.getValueAsString();

        jp.nextToken(); //registration

        String registration = jp.getValueAsString();

        jp.nextToken(); //timestamp seconds

        long timestampSeconds = jp.getValueAsLong();

        jp.nextToken(); //from

        String from = jp.getValueAsString();

        jp.nextToken(); //to

        String to = jp.getValueAsString();

        jp.nextToken(); //flight number

        String flightNumber = jp.getValueAsString();

        jp.nextToken();
        jp.nextToken(); //vertical speed
        jp.nextToken(); //callsign

        String callsign = jp.getValueAsString();

        jp.nextToken();
        jp.nextToken(); //end array

        if (locationTooFar(lon, lat)) {
            return null;
        }

        double altitude = MathUtils.feetToMeters(altitudeFeet);

        SimpleLocation planeLocation = new SimpleLocation(lon, lat, altitude);

        SimpleLocation userLocation = locationHolder.getLocation();
        Vector3D distVector = MathUtils.calculateDistanceVector(planeLocation, userLocation);

        double distance = distVector.length();
        if (distance > range)
            return null;

        Plane plane = new Plane();
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

    private boolean locationTooFar(double longtitude, double latitude) {
        SimpleLocation userLocation = locationHolder.getLocation();

        return Math.abs(longtitude - userLocation.getLongtitude()) > 2 ||
                Math.abs(latitude - userLocation.getLatitude()) > 2;
    }
}
