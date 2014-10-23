package com.mac.airspy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.google.inject.Inject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import roboguice.util.SafeAsyncTask;

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

    @Inject
    private ObjectsProvider objectsProvider;

    private ARObject currentObject;

    private LoadObjectInfoTask currentObjectInfoTask;

    public void init() {
        slidingLayout.setPanelSlideListener(this);
        setSlidingPanelVisible(false);
    }

    public void showObjectInfo(ARObject object) {

        currentObject = object;
        setSlidingPanelVisible(true);

        new LoadObjectInfoTask().execute();
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

    private class LoadObjectInfoTask extends SafeAsyncTask<View> {
        @Override
        protected void onPreExecute() throws Exception {
            if (currentObjectInfoTask != null) {
                currentObjectInfoTask.cancel(true);
            }
            currentObjectInfoTask = this;

            objectInfoContainer.setVisibility(View.GONE);
            objectDetailsContainer.setVisibility(View.GONE);
            objectInfoProgressBar.setVisibility(View.VISIBLE);
            objectDetailsProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public View call() throws Exception {
            return objectsProvider.getDetailsProvider().getInfoView(currentObject);
        }

        @Override
        protected void onSuccess(View view) throws Exception {
            objectInfoContainer.removeAllViews();
            objectInfoContainer.addView(view);

            objectInfoProgressBar.setVisibility(View.GONE);
            objectInfoContainer.setVisibility(View.VISIBLE);
        }
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
        }, 20);
    }
}
