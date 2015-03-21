package com.mac.airspy.content.source.fr24;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.R;
import com.mac.airspy.content.ObjectViewProvider;
import com.mac.airspy.content.source.fr24.dto.PlaneDetailsDto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maciej on 2015-03-21.
 */
public class FRDetailsViewProvider implements ObjectViewProvider {
    @Inject
    private LayoutInflater layoutInflater;

    @Inject
    private FlightRadarClient frClient;

    @Inject
    private Context ctx;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FRDetailsViewProvider() {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    @Override
    public View getView(ARObject object) throws IOException {
        Plane plane = (Plane) object;
        InputStream is = frClient.getPlaneDataStream(plane.getId());

        final PlaneDetailsDto planeDetails = objectMapper.readValue(is, PlaneDetailsDto.class);

        View layout = layoutInflater.inflate(R.layout.plane_details, null, false);

        bindPlaneDetailsToLayout(planeDetails, layout);

        return layout;
    }

    private View bindPlaneDetailsToLayout(final PlaneDetailsDto planeDetails, View layout) throws IOException {

        TextView fromView = (TextView) layout.findViewById(R.id.textView10);
        String fromCity = planeDetails.getFromCity();
        fromView.setText(fromCity);
        if (StringUtils.isBlank(fromCity)) {
            ((View) fromView.getParent()).setVisibility(View.GONE);
        }

        TextView toView = (TextView) layout.findViewById(R.id.textView11);
        String toCity = planeDetails.getToCity();
        toView.setText(toCity);
        if (StringUtils.isBlank(toCity)) {
            ((View) toView.getParent()).setVisibility(View.GONE);
        }

        TextView aircraftView = (TextView) layout.findViewById(R.id.textView13);
        String aircraft = planeDetails.getAircraft();
        aircraftView.setText(aircraft);
        if (StringUtils.isBlank(aircraft)) {
            ((View) aircraftView.getParent()).setVisibility(View.GONE);
        }

        TextView airlineView = (TextView) layout.findViewById(R.id.textView12);
        String airline = planeDetails.getAirline();
        airlineView.setText(airline);
        if (StringUtils.isBlank(airline)) {
            ((View) airlineView.getParent()).setVisibility(View.GONE);
        }

        final ImageView imgView = (ImageView) layout.findViewById(R.id.imageView6);
        final View progressBar = layout.findViewById(R.id.progressBar2);
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(ctx)
                        .load(planeDetails.getImageLarge())
                        .into(imgView, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);

                                imgView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(planeDetails.getImagelinkLarge()));
                                        ctx.startActivity(browserIntent);
                                    }
                                });
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        });
        return layout;
    }
}
