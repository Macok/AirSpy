package com.mac.airspy.content.source.fr24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.google.inject.Inject;
import com.mac.airspy.ARObject;
import com.mac.airspy.R;
import com.mac.airspy.content.ObjectViewProvider;

/**
 * Created by Maciej on 2015-03-21.
 */
public class FRDetailsViewProvider implements ObjectViewProvider {
    @Inject
    private LayoutInflater layoutInflater;

    @Inject
    private Context ctx;

    @Override
    public View getView(ARObject object) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        View layout = layoutInflater.inflate(R.layout.plane_details, null, false);
        return layout;
    }
}
