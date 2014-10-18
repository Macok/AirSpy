package com.mac.airspy.content.source.fr24;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Maciej on 08.01.14.
 */
public class FlightRadarClient {
    private static final String TAG = FlightRadarClient.class.getSimpleName();

    public static final String FLIGHTRADAR_URL = "http://www.flightradar24.com";
    public static final String FLIGHTRADAR_DB_URL = "http://bma.data.fr24.com";
    private static final String FLIGHTRADAR_ZONES_URL = FLIGHTRADAR_URL + "/js/zones.js.php";
    private static final String FLIGHTRADAR_DATA_URL = FLIGHTRADAR_DB_URL + "/zones/ZONE_NAME_all.json";
    private static final String FLIGHTRADAR_PLANEDATA_URL =
            FLIGHTRADAR_DB_URL + "/_external/planedata_json.1.4.php?hex=";

    public static final int CONNECT_TIMEOUT_MILLIS = 5000;
    public static final int READ_TIMEOUT_MILLIS = 8000;

    public InputStream getTrafficStream(String zone) throws IOException{
        String urlStr = FLIGHTRADAR_DATA_URL.replace("ZONE_NAME", zone);
        return openStream(urlStr);
    }

    public InputStream getZonesStream() throws IOException {
        return openStream(FLIGHTRADAR_ZONES_URL);
    }

    public InputStream getPlaneDataStream(String hex) throws IOException {
        String url = FLIGHTRADAR_PLANEDATA_URL + hex;
        Log.d(TAG, url);
        return openStream(url);
    }

    private InputStream openStream(String urlStr) throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            connection.setReadTimeout(READ_TIMEOUT_MILLIS);
            return connection.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "openStream", e);
            throw e;
        }
    }
}
