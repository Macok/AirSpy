package com.mac.airspy;

import android.util.Log;
import com.mac.airspy.content.ObjectSource;
import com.mac.airspy.content.source.fr24.FRObjectSource;
import com.mac.airspy.content.source.test.TestObjectSource;
import roboguice.RoboGuice;
import roboguice.inject.ContextSingleton;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Maciej on 2014-10-12.
 */

@ContextSingleton
public class ObjectsProvider extends BaseApplicationComponent {

    public static final int OBJECTS_UPDATE_INTERVAL_SECONDS = 60;

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

    public List<? extends ARObject> getObjects() {
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
            if (!cancelled) {
                try {
                    objects = objectSource.getObjects();
                } catch (IOException e) {
                    Log.e("", "", e);
                    setState(ComponentState.ERROR);
                    cancel();
                    return;
                }

                setState(ComponentState.READY);
            }
        }

        public void cancel() {
            cancelled = true;
        }
    }
}
