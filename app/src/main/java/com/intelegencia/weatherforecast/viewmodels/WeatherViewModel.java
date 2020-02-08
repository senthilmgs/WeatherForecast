package com.intelegencia.weatherforecast.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.intelegencia.weatherforecast.service.models.WeatherModel;

/**
 * Viewmodel class which holds the livedata to update the UI
 */
public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository mWeatherRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = new WeatherRepository(application);
    }

    public LiveData<WeatherModel> getWeatherReport(LifecycleOwner lifecycleOwner) {
        return mWeatherRepository.loadDataFromWorker(lifecycleOwner);
    }
}
