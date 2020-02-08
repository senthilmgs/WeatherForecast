package com.intelegencia.weatherforecast.service.models.common;

import com.google.gson.annotations.SerializedName;
public class Weather {

    @SerializedName("id")
    public int id;
    @SerializedName("icon")
    public String icon;
    @SerializedName("main")
    String main;
    @SerializedName("description")
    String description;

    public Weather(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}