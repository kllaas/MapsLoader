package com.klimchuk.mapsloader.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by alexey on 10.05.17.
 */

public interface MapsApi {

    @GET("/download.php?standard=yes")
    @Streaming
    Call<ResponseBody> downloadMap(@Query("file") String fileName);

}
