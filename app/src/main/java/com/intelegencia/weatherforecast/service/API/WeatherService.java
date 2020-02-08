package com.intelegencia.weatherforecast.service.API;


import com.intelegencia.weatherforecast.service.models.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherService {

    /*
     * Returns data based on the lat/lon and appid
     */
    @GET("/data/2.5/weather")
    Call<WeatherModel> getWeatherDataByLatLon(@Query("lat") String lat, @Query("lon") String lon,@Query("appid") String appId);

}