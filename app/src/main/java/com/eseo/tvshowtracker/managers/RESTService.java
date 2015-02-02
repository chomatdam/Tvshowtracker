package com.eseo.tvshowtracker.managers;

import com.eseo.tvshowtracker.model.TVShow;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RESTService {

    @GET("/search/tv")
    TVShow getTVShow(@Query("query") String name);

    @GET("/tv/{id}")
    TVShow getDetailledTVShow(@Path("id") String id);

}
