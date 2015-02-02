package com.eseo.tvshowtracker.managers;

import com.eseo.tvshowtracker.model.ResultsPage;

import retrofit.http.GET;
import retrofit.http.Query;

public interface RESTService {

    @GET("/search/tv")
    public ResultsPage getTvSeries(@Query("query") String name);

    /*
    @GET("/tv/{id}")
    TVShow getDetailledTVShow(@Path("id") String id);
    */

}
