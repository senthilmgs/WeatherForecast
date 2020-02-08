package com.intelegencia.weatherforecast.service.models.common;

import com.google.gson.annotations.SerializedName;
public class Clouds {
    public Clouds() {
    }

    @SerializedName("all")
    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
