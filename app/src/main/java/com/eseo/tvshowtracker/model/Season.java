package com.eseo.tvshowtracker.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Damien on 2/2/2015.
 */
public class Season implements Serializable {

    private int season_number;
    private int episode_count;
    private Date air_date;

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

    public Date getAir_date() {
        return air_date;
    }

    public void setAir_date(Date air_date) {
        this.air_date = air_date;
    }
}
