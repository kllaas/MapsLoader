package com.klimchuk.mapsloader.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by alexey on 09.05.17.
 */

public class MainContract {

    interface View {

        void showRegions(RecyclerView.Adapter adapter);

        Context getContext();
    }

    interface Presenter {

        void navigateBack();

        void onLoadingFinished();
    }
}
