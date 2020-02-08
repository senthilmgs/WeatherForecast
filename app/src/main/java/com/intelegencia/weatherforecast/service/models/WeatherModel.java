package com.intelegencia.weatherforecast.service.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.intelegencia.weatherforecast.service.models.common.Clouds;
import com.intelegencia.weatherforecast.service.models.common.Coord;
import com.intelegencia.weatherforecast.service.models.common.DataConverter;
import com.intelegencia.weatherforecast.service.models.common.Weather;
import com.intelegencia.weatherforecast.service.models.common.Wind;

import java.util.List;

@Entity
@TypeConverters(DataConverter.class)
public class WeatherModel {


    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("cod")
    private long cod;


    @SerializedName("visibility")
    private int visibility;

    @SerializedName("base")
    private String base;


    @Embedded(prefix = "coord")
    @SerializedName("coord")
    private Coord coord;

    @Embedded(prefix = "main")
    @SerializedName("main")
    private Main main;


    @Embedded(prefix = "wind")
    @SerializedName("wind")
    private Wind wind;

    @Embedded(prefix = "clouds")
    @SerializedName("clouds")
    private Clouds clouds;

    @Embedded(prefix = "sys")
    @SerializedName("sys")
    private Sys sys;

    @SerializedName("weather")
    private List<Weather> weather;


    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }


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

    public long getCod() {
        return cod;
    }

    public void setCod(long cod) {
        this.cod = cod;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }


    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
