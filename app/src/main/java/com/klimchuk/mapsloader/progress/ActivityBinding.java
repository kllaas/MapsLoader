package com.klimchuk.mapsloader.progress;

/**
 * Interface to communicate loading data with MainActivity.
 */

public interface ActivityBinding {

    interface ProgressFinishing {
        void onLoadingFinished();
    }

}
