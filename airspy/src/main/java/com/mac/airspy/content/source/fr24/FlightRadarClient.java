package com.mac.airspy.content.source.fr24;

import android.util.Log;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Maciej on 08.01.14.
 */
public class FlightRadarClient {
    private static final String TAG = FlightRadarClient.class.getSimpleName();

    private static final String FR_URL = "http://www.flightradar24.com";
    private static final String FR_DB_URL = "http://bma.data.fr24.com";
    private static final String FR_DATA_URL = FR_DB_URL + "/zones/fcgi/feed.json?array=1&faa=1&gnd=0&vehicles=0&bounds=";
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

    public InputStream getTrafficStream(Double[] bounds) throws IOException {
        String url = FR_DATA_URL + formatBounds(bounds);
        return openStream(url);
    }

    public InputStream getPlaneDataStream(String id) throws IOException {
        String url = FR_PLANEDATA_URL + id;
        Log.d(TAG, url);
        return openStream(url);
    }

    private InputStream openStream(String urlStr) throws IOException {
        HttpGet httpGet = new HttpGet(urlStr);

        HttpResponse response = httpClient.execute(httpGet);

        return response.getEntity().getContent();
    }

    private static String formatBounds(Double[] bounds) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.0", otherSymbols);

        StringBuilder sb = new StringBuilder();
        for (Double b : bounds) {
            sb.append(df.format(b));
            sb.append(',');
        }

        String boundsStr = sb.toString();
        return boundsStr.substring(0, boundsStr.length() - 1);
    }
}
