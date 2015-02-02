package com.eseo.tvshowtracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Damien on 2/2/2015.
 */
public class TVShow {

    @SerializedName("id")
    private int id;
    @SerializedName("original_name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
