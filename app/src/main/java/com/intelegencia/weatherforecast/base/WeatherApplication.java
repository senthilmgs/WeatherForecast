package com.intelegencia.weatherforecast.base;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intelegencia.weatherforecast.service.API.WeatherService;
import com.intelegencia.weatherforecast.utils.Constants;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


public class WeatherApplication extends Application {

    public static WeatherService initRestClient() {

        // GSON converter factory for Retrofit response
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit adapter = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return adapter.create(WeatherService.class);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

}
