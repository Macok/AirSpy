package com.mac.airspy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2014-10-21.
 */

@ContextSingleton
public class ObjectDetailsDisplay implements SlidingUpPanelLayout.PanelSlideListener {

    @InjectView(R.id.sliding_layout)
    private SlidingUpPanelLayout slidingLayout;

    @InjectView(R.id.objectInfoContainer)
    private ViewGroup objectInfoContainer;

    @InjectView(R.id.objectDetailsContainer)
    private ViewGroup objectDetailsContainer;

    @InjectView(R.id.objectInfoProgressBar)
    private View objectInfoProgressBar;

    @InjectView(R.id.objectDetailsProgressBar)
    private View objectDetailsProgressBar;

    private ARObject currentObject;

    public void init() {
        slidingLayout.setPanelSlideListener(this);
        setSlidingPanelVisible(false);
    }

    public void showObjectInfo(ARObject object) {

        currentObject = object;
        setSlidingPanelVisible(true);

        //todo load objectInfoContainer
    }

    public ARObject getCurrentObject() {
        return currentObject;
    }

    public void hide() {
        currentObject = null;
        setSlidingPanelVisible(false);
    }

    @Override
    public void onPanelSlide(View view, float v) {
        //todo load objectDetailsContainer
    }

    @Override
    public void onPanelCollapsed(View view) {

    }

    @Override
    public void onPanelExpanded(View view) {

    }

    @Override
    public void onPanelAnchored(View view) {

    }

    @Override
    public void onPanelHidden(View view) {

    }

    private void setSlidingPanelVisible(final boolean visible) {

        //SlidingUpPanelLayout bug walkaround
        slidingLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    slidingLayout.showPanel();
                } else {
                    slidingLayout.hidePanel();
                }
            }
        }, 10);
    }
}
