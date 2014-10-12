package com.mac.airspy;

import roboguice.RoboGuice;
import roboguice.inject.ContextSingleton;

/**
 * Created by Maciej on 2014-10-11.
 */

@ContextSingleton
public class MainLoopController extends BaseApplicationComponent {

    private MainLoop mainLoop;

    public void start() {
        mainLoop = new MainLoop();
        RoboGuice.getInjector(ctx).injectMembers(mainLoop);
        new Thread(mainLoop).start();

        setState(ComponentState.READY);
    }

    public void stop() {
        if (mainLoop != null) {
            mainLoop.stop();
        }

        setState(ComponentState.STOPPED);
    }
}
