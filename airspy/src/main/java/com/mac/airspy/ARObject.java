package com.mac.airspy;

import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.Vector3D;

/**
 * Created by Maciej on 02.02.14.
 */
public interface ARObject {
    public Object getId();

    public String getName();

    public Vector3D getApproximatedDistanceVector();
    public void setApproximatedDistanceVector(Vector3D approximatedDistanceVector);

    public SimpleLocation getLocation();
}
