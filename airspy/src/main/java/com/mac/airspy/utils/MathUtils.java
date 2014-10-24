package com.mac.airspy.utils;


import com.mac.airspy.ARObject;
import com.mac.airspy.MovingARObject;
import com.mac.airspy.location.SimpleLocation;

/**
 * Created by Maciej on 02.02.14.
 */
public class MathUtils {
    public static final double FEET_TO_METERS = 0.3048;
    public static final double KNOTS_TO_KMH = 1.852;
    public static final int EARTH_RADIUS = 6371;
    public static final long MILLIS_IN_HOUR = 3600 * 1000;

    public static double knotsToKmh(double knots) {
        return knots * KNOTS_TO_KMH;
    }

    public static double feetToMeters(double feet) {
        return feet * FEET_TO_METERS;
    }

    public static Vector3D calculateApproximatedDistanceVector(SimpleLocation l1, ARObject object) {
        Vector3D distanceVector = calculateDistanceVector(l1, object.getLocation());

        if (object instanceof MovingARObject) {
            Vector3D dislocationVector = calculateDislocation((MovingARObject) object);
            return distanceVector.add(dislocationVector);
        }

        return distanceVector;
    }

    public static Vector3D calculateDistanceVector(SimpleLocation l1, SimpleLocation l2) {
        double l1Lat = l1.getLatitude();
        double l1Long = l1.getLongtitude();

        double l2Lat = l2.getLatitude();
        double l2Long = l2.getLongtitude();

        double dLat =  Math.toRadians(l2Lat - l1Lat);
        double dLong = Math.toRadians(l2Long - l1Long);

        double distY = dLat * EARTH_RADIUS;

        if (dLong > Math.PI)
            dLong -= 2 * Math.PI;
        else if (dLong < -Math.PI)
            dLong += 2 * Math.PI;

        float R2 = EARTH_RADIUS * (float) Math.abs(Math.cos((l1Lat + l2Lat) / 2));

        double distX = R2 * dLong;

        double distZ = l2.getAltitude()-l1.getAltitude();

        return new Vector3D(distX, distY, distZ);
    }

    private static Vector3D calculateDislocation(MovingARObject object) {
        long dt = System.currentTimeMillis() - object.getLastUpdateTime();
        double distanceCovered = object.getSpeedKmh() * ((double) dt / MILLIS_IN_HOUR);

        double track = Math.toRadians(object.getTrack());
        double dx = Math.sin(track) * distanceCovered;
        double dy = Math.cos(track) * distanceCovered;

        return new Vector3D(dx, dy, 0);
    }
}
