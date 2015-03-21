package com.mac.airspy.content.source.test;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.content.ObjectInfoViewProvider;
import com.mac.airspy.content.ObjectSource;
import com.mac.airspy.location.SimpleLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-10-12.
 */
public class TestObjectSource implements ObjectSource {
    @Inject
    private Context ctx;

    @Override
    public List<? extends ARObject> getObjects() {
        List<ARObject> list = new ArrayList<ARObject>();

        SimpleLocation palacKultury = new SimpleLocation(21.006111, 52.231667, 0.120);
        SimpleLocation factory = new SimpleLocation(20.8946992, 52.2012648, 0.40);
        SimpleLocation castorama = new SimpleLocation(20.9335608, 52.2030393, 0.40);
        list.add(new TestObject("Palac kultury", palacKultury));
        list.add(new TestObject("factory", factory));
        list.add(new TestObject("castorama", castorama));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public ObjectInfoViewProvider getInfoViewProvider() {
        return new ObjectInfoViewProvider() {
            @Override
            public View getInfoView(ARObject object) {
                TextView view = new TextView(ctx);
                view.setText("Obiekt testowy: " + object.getName());
                return view;
            }
        };
    }
}
