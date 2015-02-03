package com.eseo.tvshowtracker.managers;

import com.eseo.tvshowtracker.model.SearchResultsPage;
import com.eseo.tvshowtracker.model.TvShow;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RESTService {

    @GET("/search/tv")
    public void getTvShows(@Query("query") String name, Callback<SearchResultsPage> callback);

    @GET("/tv/popular")
    public void getPopularTvShows(Callback<SearchResultsPage> callback);

    @GET("/tv/{id}")
    public void getDetailledTVShow(@Path("id") String id, Callback<TvShow> callback);


}
