package com.mac.airspy;

import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import roboguice.util.SafeAsyncTask;

/**
 * Created by Maciej on 2014-10-21.
 */

@ContextSingleton
public class ObjectDetailsDisplay implements SlidingUpPanelLayout.PanelSlideListener {

    private static final int DOUBLE_CLICK_INTERVAL_MILLIS = 300;

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
    private LoadObjectDetailsTask currentObjectDetailsTask;

    private long lastObjectChangeTime = 0;

    public void init() {
        slidingLayout.setPanelSlideListener(this);

        //AndroidSlidingUpPanel bug walkaroung
        slidingLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                hidePanel();
            }
        }, 1000);
    }

    private void hidePanel() {
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    public void showObjectInfo(ARObject object) {
        if (!intervalElapsed()) {
            return; //ignore double clicks
        }

        stopCurrentTasks();

        currentObject = object;
        setSlidingPanelVisible(true);

        currentObjectInfoTask = new LoadObjectInfoTask();
        currentObjectInfoTask.execute();

    }

    private boolean intervalElapsed() {
        long time = System.currentTimeMillis();
        if (time - lastObjectChangeTime > DOUBLE_CLICK_INTERVAL_MILLIS) {
            lastObjectChangeTime = time;
            return true;
        }

        return false;
    }

    public ARObject getCurrentObject() {
        return currentObject;
    }

    public void hide() {
        if (!intervalElapsed()) {
            return;
        }

        stopCurrentTasks();

        currentObject = null;
        setSlidingPanelVisible(false);
    }

    private void stopCurrentTasks() {
        if (currentObjectInfoTask != null) {
            currentObjectInfoTask.cancel(false);
            currentObjectInfoTask = null;
        }

        if (currentObjectDetailsTask != null) {
            currentObjectDetailsTask.cancel(false);
            currentObjectDetailsTask = null;
        }
    }

    @Override
    public void onPanelSlide(View view, float v) {
        if (currentObject != null) {
            if (currentObjectDetailsTask == null) {
                currentObjectDetailsTask = new LoadObjectDetailsTask();
                currentObjectDetailsTask.execute();
            }
        }
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
        slidingLayout.post(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    showPanel();
                } else {
                    hidePanel();
                }
            }
        });
    }

    private void showPanel() {
        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private class LoadObjectInfoTask extends SafeAsyncTask<View> {
        @Override
        protected void onPreExecute() throws Exception {
            objectInfoContainer.setVisibility(View.GONE);
            objectDetailsContainer.setVisibility(View.GONE);
            objectInfoProgressBar.setVisibility(View.VISIBLE);
            objectDetailsProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public View call() throws Exception {
            return objectsProvider.getInfoViewProvider().getView(currentObject);
        }

        @Override
        protected void onSuccess(View view) throws Exception {
            objectInfoContainer.removeAllViews();
            objectInfoContainer.addView(view);

            objectInfoProgressBar.setVisibility(View.GONE);
            objectInfoContainer.setVisibility(View.VISIBLE);
        }
    }

    private class LoadObjectDetailsTask extends SafeAsyncTask<View>{
        @Override
        protected void onPreExecute() throws Exception {
            objectDetailsContainer.setVisibility(View.GONE);
            objectDetailsProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public View call() throws Exception {
            return objectsProvider.getDetailsViewProvider().getView(currentObject);
        }

        @Override
        protected void onSuccess(View view) throws Exception {
            objectDetailsContainer.removeAllViews();
            objectDetailsContainer.addView(view);

            objectDetailsContainer.setVisibility(View.VISIBLE);
            objectDetailsProgressBar.setVisibility(View.GONE);
        }
    }
}
