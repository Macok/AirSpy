package com.mac.airspy.content.source.fr24;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.R;
import com.mac.airspy.content.ObjectDetailsViewProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Maciej on 2014-10-23.
 */
public class FRDetailsViewProvider implements ObjectDetailsViewProvider {
    @Inject
    private LayoutInflater layoutInflater;

    @Inject
    private Context ctx;

    private static final NumberFormat numberFormat = new DecimalFormat("#0");

    @Override
    public View getInfoView(ARObject object) {
        final Plane plane = (Plane) object;

        View layout = layoutInflater.inflate(R.layout.plane_info, null, false);

        bindPlaneToLayout(plane, layout);

        return layout;
    }

    private void bindPlaneToLayout(final Plane plane, View layout) {
        TextView fromView = (TextView) layout.findViewById(R.id.textView);
        TextView toView = (TextView) layout.findViewById(R.id.textView2);
        TextView aircraftView = (TextView) layout.findViewById(R.id.textView6);
        TextView speedView = (TextView) layout.findViewById(R.id.textView5);

        if (!StringUtils.isBlank(plane.getFromCode())) {
            fromView.setText(plane.getFromCode());
        }else {
            ((View) fromView.getParent()).setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.isBlank(plane.getToCode())) {
            toView.setText(plane.getToCode());
        }else {
            ((View) toView.getParent()).setVisibility(View.INVISIBLE);
        }

        if (!StringUtils.isBlank(plane.getAircraftCode())) {
            aircraftView.setText(plane.getAircraftCode());
        }else {
            ((View) aircraftView.getParent()).setVisibility(View.INVISIBLE);
        }

        speedView.setText(numberFormat.format(plane.getSpeedKmh()) + " km/h");

        final ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
        final View progressBar = layout.findViewById(R.id.progressBar);
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageLoader.getInstance().displayImage(
                        FlightRadarClient.FR_AIRCRAFT_THUMBNAIL_URL
                                .replace("AIRCRAFT_CODE", plane.getAircraftCode()),
                        imageView,
                        new LoadingViewManagingListener(progressBar)
                );

            }
        });
    }

    private static class LoadingViewManagingListener implements ImageLoadingListener {
        private final View loadingView;

        private LoadingViewManagingListener(View loadingView) {
            this.loadingView = loadingView;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            loadingView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            loadingView.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            loadingView.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };
}
