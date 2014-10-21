package com.mac.airspy.content.source.fr24;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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
    public static final int SOCKET_TIMEOUT_MILLIS = 1000;
    private HttpClient httpClient;

    public FlightRadarClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT_MILLIS);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT_MILLIS);

        httpClient = new DefaultHttpClient(params);
    }

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
        HttpGet httpGet = new HttpGet(urlStr);

        HttpResponse response = httpClient.execute(httpGet);

        return response.getEntity().getContent();
    }
}
