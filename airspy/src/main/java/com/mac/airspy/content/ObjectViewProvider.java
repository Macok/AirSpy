package com.mac.airspy.content;

import android.view.View;
import com.mac.airspy.ARObject;

import java.io.IOException;

/**
 * Created by Maciej on 2014-10-23.
 */
public interface ObjectViewProvider {
    public View getView(ARObject object) throws IOException;
}
