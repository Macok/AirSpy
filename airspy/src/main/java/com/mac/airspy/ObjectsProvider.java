package com.mac.airspy;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.google.inject.Inject;
import com.mac.airspy.content.ObjectViewProvider;
import com.mac.airspy.content.ObjectSource;
import com.mac.airspy.content.source.fr24.FRObjectSource;
import roboguice.RoboGuice;
import roboguice.inject.ContextSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Maciej on 2014-10-12.
 */

@ContextSingleton
public class ObjectsProvider extends BaseApplicationComponent {

    public static final int RANGE_MAX_KM = 200;
    public static final int RANGE_MIN_KM = 5;
    public static final int RANGE_DEFAULT_KM = 60;

    public static final int OBJECTS_UPDATE_INTERVAL_SECONDS = 30;

    @Inject
    private UserPreferencesHelper preferencesHelper;

    private ScheduledExecutorService executor;

    private List<? extends ARObject> objects;

    private ObjectSource objectSource;

    private UpdateObjectsCommand currentUpdateCommand;

    public void start() {
        objectSource = new FRObjectSource();
        RoboGuice.getInjector(ctx).injectMembers(objectSource);

        setState(objects == null ? ComponentState.STARTING : ComponentState.READY);

        currentUpdateCommand = new UpdateObjectsCommand(objectSource);
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(currentUpdateCommand, 0, OBJECTS_UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public void stop() {
        setState(ComponentState.STOPPED);

        currentUpdateCommand.cancel();

        objectSource = null;

        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

    public List<? extends ARObject> getObjectsInRange() {
        List<ARObject> objectsInRange = new ArrayList<>();
        for (ARObject object : objects) {
            if (object.getApproximatedDistanceVector().length() <= preferencesHelper.getRange()) {
                objectsInRange.add(object);
            }
        }

        return objectsInRange;
    }

    public List<? extends ARObject> getAllObjects() {
        return objects;
    }

    private class UpdateObjectsCommand implements Runnable {

        private final ObjectSource objectSource;


        private boolean cancelled;
        private UpdateObjectsCommand(ObjectSource objectSource) {
            this.objectSource = objectSource;
        }

        @Override
        public void run() {
            try {
                List<? extends ARObject> newObjects = objectSource.getObjects();
                if (!cancelled) {
                    objects = newObjects;
                }
            } catch (IOException e) {
                Log.e("", "", e);
                setState(ComponentState.ERROR);
                cancel();
                return;
            }

            //todo delete
            ((Activity)ctx).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx, "Objects updated", Toast.LENGTH_SHORT).show();
                }
            });


            setState(ComponentState.READY);

        }

        private void cancel() {
            cancelled = true;
        }

    }

    public ObjectViewProvider getInfoViewProvider() {
        return objectSource.getInfoViewProvider();
    }

    public ObjectViewProvider getDetailsViewProvider() {
        return objectSource.getDetailsViewProvider();
    }
}
