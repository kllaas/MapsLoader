package com.klimchuk.mapsloader.progress;

import android.content.Context;

/**
 * Created by alexey on 04.05.17.
 */

interface ProgressContract {


    interface View {

        void setProgress(int progress);

        Context getActivityContext();

        void setProgressText(String text);

        void setProgressTitle(int text);
    }

    interface Presenter {

    }
}
