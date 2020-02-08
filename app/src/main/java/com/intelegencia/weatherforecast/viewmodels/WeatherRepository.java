package com.intelegencia.weatherforecast.viewmodels;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.util.concurrent.ListenableFuture;
import com.intelegencia.weatherforecast.db.AppExecutors;
import com.intelegencia.weatherforecast.db.WeatherDatabase;
import com.intelegencia.weatherforecast.service.models.WeatherModel;
import com.intelegencia.weatherforecast.workmanager.LocationWorker;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * WeatherRepository leads to handle the livedata to update the viewmodel with Roomdatabase
 */

public class WeatherRepository {

    private static String SMART_WORKER = "myWeatherWorker";
    private MutableLiveData<WeatherModel> mutableLiveData = new MutableLiveData<WeatherModel>();
    private Application mApplication;
    private WeatherModel weatherModel;


    //Schedule a worker to fetch the data periodically from the server
    public WeatherRepository(Application application) {
        this.mApplication = application;
    }

    public MutableLiveData<WeatherModel> loadDataFromWorker(LifecycleOwner lifecycleOwner) {

        if (!isWorkScheduled()) {
            scheduleWeather(lifecycleOwner);
        } else {
            getWeatherDataFromDb();
        }
        return mutableLiveData;
    }

    // Check whether the workManager already schedued the given locaion
    private boolean isWorkScheduled() {
        WorkManager instance = WorkManager.getInstance(mApplication);
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(SMART_WORKER);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Schedule the worker with periodicWorkrequest with interval of of 2:00 hours once
    private void scheduleWeather(LifecycleOwner lifecycleOwner) {
        PeriodicWorkRequest periodicRequest =
                new PeriodicWorkRequest.Builder(LocationWorker.class, 2, TimeUnit.HOURS)
                        .addTag(SMART_WORKER)
                        .build();
        WorkManager workManager = WorkManager.getInstance(mApplication);
        workManager.enqueueUniquePeriodicWork(SMART_WORKER,
                ExistingPeriodicWorkPolicy.KEEP, periodicRequest);
        workManager.getWorkInfoByIdLiveData(periodicRequest.getId())
                .observe(lifecycleOwner, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            getWeatherDataFromDb();
                        }
                    }
                });
    }

    private void getWeatherDataFromDb() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                WeatherDatabase mDb = WeatherDatabase.getInstance(mApplication);
                weatherModel = mDb.daoAccess().fetchAllData();
                mutableLiveData.postValue(weatherModel);

            }
        });
    }

}