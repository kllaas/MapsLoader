package com.klimchuk.mapsloader;

import android.app.Application;

import com.klimchuk.mapsloader.data.MapsApi;
import com.klimchuk.mapsloader.data.StaticDataCache;

import retrofit2.Retrofit;

/**
 * Created by alexey on 10.05.17.
 */

public class App extends Application {

    private static MapsApi mapsApi;

    public static MapsApi getMapsApi() {
        return mapsApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://download.osmand.net")
                .build();

        mapsApi = retrofit.create(MapsApi.class);

        StaticDataCache.loadRegions(this);
    }
}
