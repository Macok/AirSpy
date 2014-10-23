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

/**
 * Created by Maciej on 08.01.14.
 */
public class FlightRadarClient {
    private static final String TAG = FlightRadarClient.class.getSimpleName();

    private static final String FR_URL = "http://www.flightradar24.com";
    private static final String FR_DB_URL = "http://bma.data.fr24.com";
    private static final String FR_ZONES_URL = FR_URL + "/js/zones.js.php";
    private static final String FR_DATA_URL = FR_DB_URL + "/zones/ZONE_NAME_all.json";
    private static final String FR_PLANEDATA_URL =
            FR_DB_URL + "/_external/planedata_json.1.4.php?hex=";

    public static final String FR_AIRCRAFT_THUMBNAIL_URL =
            "http://flightradar24static.appspot.com/static/_fr24/images/sideviews/AIRCRAFT_CODE.png";

    public static final int CONNECT_TIMEOUT_MILLIS = 8000;
    public static final int SOCKET_TIMEOUT_MILLIS = 5000;
    private HttpClient httpClient;

    public FlightRadarClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT_MILLIS);
        HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT_MILLIS);

        httpClient = new DefaultHttpClient(params);
    }

    public InputStream getTrafficStream(String zone) throws IOException{
        String urlStr = FR_DATA_URL.replace("ZONE_NAME", zone);
        return openStream(urlStr);
    }

    public InputStream getZonesStream() throws IOException {
        return openStream(FR_ZONES_URL);
    }

    public InputStream getPlaneDataStream(String hex) throws IOException {
        String url = FR_PLANEDATA_URL + hex;
        Log.d(TAG, url);
        return openStream(url);
    }

    private InputStream openStream(String urlStr) throws IOException {
        HttpGet httpGet = new HttpGet(urlStr);

        HttpResponse response = httpClient.execute(httpGet);

        return response.getEntity().getContent();
    }
}
