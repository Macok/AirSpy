package com.mac.airspy;

import android.content.SharedPreferences;
import com.google.inject.Inject;

/**
 * Created by Maciej on 2015-03-22.
 */
public class UserPreferencesHelper {
    @Inject
    private SharedPreferences sharedPrefs;

    public void setRange(int range) {
        sharedPrefs.edit()
                .putInt("range", range)
                .apply();
    }

    public int getRange() {
        return sharedPrefs.getInt("range", ObjectsProvider.RANGE_DEFAULT_KM);
    }

    public boolean isRadarVisible() {
        return sharedPrefs.getBoolean("radarVisible", true);
    }

    public void setRadarVisible(boolean radarVisible) {
        sharedPrefs.edit()
                .putBoolean("radarVisible", radarVisible)
                .apply();
    }
}
