package com.mac.airspy.content.source.test;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.content.ObjectViewProvider;
import com.mac.airspy.content.ObjectSource;
import com.mac.airspy.location.LocationService;
import com.mac.airspy.location.SimpleLocation;
import com.mac.airspy.utils.MathUtils;
import com.mac.airspy.utils.Vector3D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-12.
 */
public class TestObjectSource implements ObjectSource {
    @Inject
    private Context ctx;

    @Inject
    private LocationService locationService;

    @Override
    public List<? extends ARObject> getObjects() {
        List<ARObject> list = new ArrayList<>();

        SimpleLocation palacKultury = new SimpleLocation(21.006111, 52.231667, 0.120);
        SimpleLocation factory = new SimpleLocation(20.8946992, 52.2012648, 0.40);
        SimpleLocation castorama = new SimpleLocation(20.9335608, 52.2030393, 0.40);
        list.add(new TestObject("Palac kultury", palacKultury));
        list.add(new TestObject("Factory", factory));
        list.add(new TestObject("Castorama", castorama));

        for (ARObject object : list) {
            Vector3D distVector = MathUtils.calculateApproximatedDistanceVector(locationService.getLocation(), object);
            object.setApproximatedDistanceVector(distVector);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public ObjectViewProvider getInfoViewProvider() {
        return new ObjectViewProvider() {
            @Override
            public View getView(ARObject object) {
                TextView view = new TextView(ctx);
                view.setText("Obiekt testowy: " + object.getName());
                return view;
            }
        };
    }

    @Override
    public ObjectViewProvider getDetailsViewProvider() {
        return new ObjectViewProvider() {
            @Override
            public View getView(ARObject object) throws IOException {
                return new LinearLayout(ctx);
            }
        };
    }
}
