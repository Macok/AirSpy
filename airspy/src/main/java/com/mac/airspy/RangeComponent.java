package com.mac.airspy;

import android.widget.SeekBar;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Created by Maciej on 2015-03-22.
 */
@ContextSingleton
public class RangeComponent implements SeekBar.OnSeekBarChangeListener {

    @InjectView(R.id.seekBar)
    private SeekBar rangeSeekBar;

    @InjectView(R.id.textView16)
    private TextView rangeTextView;

    @Inject
    private UserPreferencesHelper preferencesHelper;

    public void init() {
        rangeSeekBar.setMax(ObjectsProvider.RANGE_MAX_KM - ObjectsProvider.RANGE_MIN_KM);

        rangeSeekBar.setOnSeekBarChangeListener(this);

        rangeSeekBar.setProgress(preferencesHelper.getRange() - ObjectsProvider.RANGE_MIN_KM);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int range = seekBar.getProgress() + ObjectsProvider.RANGE_MIN_KM;
        rangeTextView.setText("" + range);

        preferencesHelper.setRange(range);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
