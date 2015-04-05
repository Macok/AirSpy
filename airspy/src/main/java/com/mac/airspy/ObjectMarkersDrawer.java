package com.mac.airspy;

import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.inject.Inject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Maciej on 2015-04-05.
 */
public class ObjectMarkersDrawer {

    private final NumberFormat numberFormat;

    @Inject
    private ObjectDetailsDisplay objectDetailsDisplay;

    private View marker;

    @Inject
    public ObjectMarkersDrawer(LayoutInflater inflater) {
        marker = inflater.inflate(R.layout.marker, null, false);

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator('.');
        numberFormat = new DecimalFormat("#0.0", formatSymbols);
    }

    public void drawMarkers(Canvas canvas, List<ObjectOnScreen> objects) {
        for (ObjectOnScreen object : objects) {
            canvas.save();
            canvas.translate(object.position.x, object.position.y);

            View marker = getMarkerForObject(object.object);
            canvas.translate(-marker.getWidth() / 2f, -marker.getHeight());
            marker.draw(canvas);
            canvas.restore();
        }
    }

    private View getMarkerForObject(ARObject object) {
        TextView nameView = (TextView) marker.findViewById(R.id.textView);
        TextView distanceView = (TextView) marker.findViewById(R.id.textView2);

        if (hasFocus(object)) {
            marker.setBackgroundResource(R.drawable.bg_active);
        }else{
            marker.setBackgroundResource(R.drawable.bg);
        }

        nameView.setText(object.getName());

        String distanceStr = numberFormat.format(object.getApproximatedDistanceVector().length());
        distanceView.setText(distanceStr + " km");

        int spec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED);
        marker.measure(spec, spec);
        marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());

        return marker;
    }

    private boolean hasFocus(ARObject object) {
        ARObject objectWithFocus = objectDetailsDisplay.getCurrentObject();
        return objectWithFocus != null &&
                objectWithFocus.getId().equals(object.getId());
    }

}
