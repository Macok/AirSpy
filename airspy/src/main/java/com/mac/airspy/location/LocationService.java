package com.mac.airspy.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.view.View;
import com.google.inject.Inject;
import com.mac.airspy.ApplicationComponent;
import com.mac.airspy.ComponentState;
import com.mac.airspy.R;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

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

    private Intent backgroungServiceIntent;

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

    public void init() {
        backgroungServiceIntent = new Intent(ctx, BackgroundLocationService.class);
        ctx.startService(backgroungServiceIntent);
    }

    public void release() {
        ctx.stopService(backgroungServiceIntent);
    }

    public void start() {
        ctx.bindService(backgroungServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        state = ComponentState.STARTING;
    }

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
