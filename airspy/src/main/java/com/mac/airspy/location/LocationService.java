package com.mac.airspy.location;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.view.View;
import com.google.inject.Inject;
import com.mac.airspy.*;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2014-10-03.
 */
@ContextSingleton
public class LocationService extends BaseApplicationComponent implements BackgroundLocationService.LocationListener {

    private Location currentLocation;

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

        state = ComponentState.STOPPED;
    }

    public SimpleLocation getLocation() {
        if (state != ComponentState.READY) {
            throw new IllegalStateException();
        }

        return new SimpleLocation(currentLocation);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;

        state = ComponentState.READY;
    }
}
