package com.mac.airspy.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import com.google.inject.Inject;
import com.mac.airspy.ApplicationComponent;
import com.mac.airspy.ComponentState;
import roboguice.inject.ContextSingleton;

/**
 * Created by Maciej on 2014-10-03.
 */
@ContextSingleton
public class LocationService implements ApplicationComponent, BackgroundLocationService.LocationListener {
    @Inject
    private Context ctx;

    private Location currentLocation;

    private ComponentState state = ComponentState.STOPPED;

    private BackgroundLocationService bgLocationService;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            bgLocationService =
                    ((BackgroundLocationService.LocalBinder) binder).getServiceInstance();

            bgLocationService.setListener(LocationService.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bgLocationService = null;

            state = ComponentState.STOPPED;
        }
    };

    @Override
    public void start() {
        Intent serviceIntent = new Intent(ctx, BackgroundLocationService.class);
        ctx.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        state = ComponentState.STARTING;
    }

    @Override
    public void stop() {
        if (bgLocationService != null) {
            bgLocationService.setListener(null);
            ctx.unbindService(serviceConnection);
            bgLocationService = null;
        }

        state = ComponentState.STOPPED ;
    }

    public Location getLocation() {
        if (state != ComponentState.READY) {
            throw new IllegalStateException();
        }

        return currentLocation;
    }

    @Override
    public ComponentState getState() {
        return state;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        state = ComponentState.READY;
    }
}
