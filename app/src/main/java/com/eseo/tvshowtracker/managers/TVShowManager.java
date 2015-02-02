package com.eseo.tvshowtracker.managers;


import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Response;

public class TVShowManager {

    private static TVShowManager instance = null;
    private RESTService service = null;
    private static final String URL = "http://api.themoviedb.org/3";
    private static final String APIKEY = "85942e21532423f95b0630459fa4101f";

    public TVShowManager(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(URL)
                .setRequestInterceptor(requestInterceptor)
                .build();
        service =restAdapter.create(RESTService.class);
    }

    public static TVShowManager getInstance(){
        if(instance == null){
            instance = new TVShowManager();
        }
        return instance;
    }

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam("api_key", APIKEY);
        }
    };

    public RESTService getService(){
        return service;
    }

}
