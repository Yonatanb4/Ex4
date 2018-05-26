package com.example.yonatan.ex4;

import com.google.gson.JsonElement;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;


public interface ImgurService {

    @Headers("Authorization: Client-ID " + "6bdae409a9f76e1")
    @GET("/album/{albumHash}")
    void getAlbumImages(@Path("albumHash") String albumHash,  Callback<JsonElement> cb);
}