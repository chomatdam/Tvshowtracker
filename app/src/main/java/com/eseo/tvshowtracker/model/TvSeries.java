package com.eseo.tvshowtracker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Damien on 2/2/2015.
 */
public class TvSeries implements Serializable{

    private int id;
    private String original_name;

    public TvSeries(int id, String name){
        this.id=id;
        this.original_name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return original_name;
    }

    public void setName(String name) {
        this.original_name = name;
    }
}
