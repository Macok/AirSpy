package com.mac.airspy.content;

import com.mac.airspy.ARObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Maciej on 2014-10-12.
 */
public interface ObjectSource {
    public List<? extends ARObject> getObjects() throws IOException;

    public ObjectDetailsViewProvider getDetailsProvider();
}
