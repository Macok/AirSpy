package com.mac.airspy;

import android.graphics.PointF;
import com.google.inject.Inject;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.parameters.CameraParameters;
import com.mac.airspy.parameters.ScreenParameters;
import com.mac.airspy.utils.MathUtils;
import com.mac.airspy.utils.Vector3D;

/**
 * Created by Maciej on 2014-10-05.
 */
public class ScreenPositionCalculator {

    public static final int SCREEN_VISIBILITY_MARGIN_PX = 100;

    @Inject
    private ARLayer arLayer;

    @Inject
    private LocationService locationService;

    @Inject
    private OrientationService orientationService;

    @Inject
    private CameraController cameraController;

    private boolean initialized;

    private ScreenParameters screenParameters;
    private CameraParameters cameraParameters;

    public PointF calculateScreenPosition(SimpleLocation targetLocation) {
        if (!initialized) {
            screenParameters = arLayer.getScreenParameters();
            cameraParameters = cameraController.getCameraParameters();
            initialized = true;
        }

        return doCalculateScreenPosition(targetLocation);
    }

    private PointF doCalculateScreenPosition(SimpleLocation targetLocation) {

        SimpleLocation observerLocation = locationService.getLocation();

        float[] orientationValues = orientationService.getOrientation();

        Vector3D distanceVector = MathUtils.calculateDistanceVector(observerLocation, targetLocation);

        double distX = distanceVector.getX();
        double distY = distanceVector.getY();
        double distZ = distanceVector.getZ();

        double r = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
        double phi = Math.atan(distX / distY);
        double theta = Math.asin(distZ / r);

        if (distY < 0) {
            phi += phi > 0 ? -Math.PI : Math.PI;
        }

        double deltaPhi = phi - orientationValues[0];
        if (deltaPhi > Math.PI)
            deltaPhi -= 2 * Math.PI;
        else if (deltaPhi < -Math.PI)
            deltaPhi += 2 * Math.PI;

        if (Math.abs(deltaPhi) > Math.PI / 2)
            return null;

        double posX = (screenParameters.sizeX / 2) * Math.tan(deltaPhi) /
                Math.tan(cameraParameters.horizontalViewAngle / 2);

        double deltaTheta = theta - orientationValues[1];

        if (Math.abs(deltaTheta) > Math.PI / 2)
            return null;

        double posY = -(screenParameters.sizeY / 2) * Math.tan(deltaTheta) /
                Math.tan(cameraParameters.verticalViewAngle / 2);

        posX = screenParameters.sizeX / 2 + posX;
        posY = screenParameters.sizeY / 2 + posY;

        if (isOutOfScreen(posX, posY)) {
            return null;
        }

        return new PointF((float) posX, (float) posY);

    }

    private boolean isOutOfScreen(double posX, double posY) {
        if (posX < 0 - SCREEN_VISIBILITY_MARGIN_PX)
            return true;
        if (posX > screenParameters.sizeX + SCREEN_VISIBILITY_MARGIN_PX)
            return true;
        if (posY < 0 - SCREEN_VISIBILITY_MARGIN_PX)
            return true;
        if (posY > screenParameters.sizeY + SCREEN_VISIBILITY_MARGIN_PX)
            return true;

        return false;
    }
}
