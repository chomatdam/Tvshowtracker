package com.eseo.tvshowtracker.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Damien on 2/2/2015.
 */
public class TvShow implements Serializable{

    private long id;
    private String original_name;
    private String poster_path ;
    private List<Season> seasons ;
    private String nextEpisode;
    private String overview;

    public TvShow(){
    }

    public TvShow(int id, String name, String poster_url){
        this.id=id;
        this.original_name=name;
        this.poster_path = poster_url ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return original_name;
    }

    public void setName(String name) {
        this.original_name = name;
    }

    public String getPoster_url() {return poster_path;}

    public void setPoster_url(String poster_url) { this.poster_path = poster_url;}

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(String nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
