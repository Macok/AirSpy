package com.mac.airspy.content.source.fr24.zone;

import com.fasterxml.jackson.databind.JsonNode;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.MathUtils;
import com.mac.airspy.utils.Vector3D;

/**
 * Created by Maciej on 01.03.14.
 */

public class Zone {

    private String name;

    private SimpleLocation topLeft;
    private SimpleLocation bottomRight;

    private Double area;

    public Zone(SimpleLocation topLeft, SimpleLocation bottomRight) {
        this(null, topLeft, bottomRight);
    }

    public Zone(String name, SimpleLocation topLeft, SimpleLocation bottomRight) {
        this.name=name;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        if (area == null) {
            area = calculateArea();
        }

        return area;
    }

    private double calculateArea() {
        Vector3D sizeVector = MathUtils.calculateDistanceVector(topLeft, bottomRight);

        return Math.abs(sizeVector.getX()) * Math.abs(sizeVector.getY());
    }

    public boolean contains(SimpleLocation location, int margin) {
        Vector3D distToTopLeft = MathUtils.calculateDistanceVector(location, topLeft);
        Vector3D distToBottomRight = MathUtils.calculateDistanceVector(location, bottomRight);

        if (distToTopLeft.getX() > -margin)
            return false;

        if (distToTopLeft.getY() < margin)
            return false;

        if (distToBottomRight.getX() < margin)
            return false;

        if (distToBottomRight.getY() > -margin)
            return false;

        return true;
    }

    public static Zone fromNode(JsonNode node) {
        double tl_y = node.get("tl_y").asDouble();
        double tl_x = node.get("tl_x").asDouble();
        double br_y = node.get("br_y").asDouble();
        double br_x = node.get("br_x").asDouble();

        return new Zone(
                new SimpleLocation(tl_x, tl_y, 0),
                new SimpleLocation(br_x, br_y, 0)
        );
    }
}