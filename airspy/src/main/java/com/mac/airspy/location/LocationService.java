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

            setState(ComponentState.STOPPED);
        }
    };

    public void start() {
        Intent bgServiceIntent = new Intent(ctx, BackgroundLocationService.class);
        ctx.bindService(bgServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        setState(ComponentState.STARTING);
    }

    public void stop() {
        if (bgLocationService != null) {
            bgLocationService.setListener(null);
            bgLocationService = null;
        }

        ctx.unbindService(serviceConnection);

        setState(ComponentState.STOPPED);
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

        setState(ComponentState.READY);
    }
}
