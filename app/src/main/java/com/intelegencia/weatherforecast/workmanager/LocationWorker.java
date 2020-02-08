package com.intelegencia.weatherforecast.workmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.intelegencia.weatherforecast.base.WeatherApplication;
import com.intelegencia.weatherforecast.db.AppExecutors;
import com.intelegencia.weatherforecast.db.WeatherDatabase;
import com.intelegencia.weatherforecast.service.API.WeatherService;
import com.intelegencia.weatherforecast.service.models.WeatherModel;
import com.intelegencia.weatherforecast.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LocationWorker class which leads to get the current location on every execuion of workrequest to served with workManager
 */
public class LocationWorker extends ListenableWorker {

    private ResolvableFuture<Result> mFuture;
    private WeatherService mRestInterface;

    public LocationWorker(@NonNull final Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    /**
     *
     * @return mFuture
     */
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        mFuture = ResolvableFuture.create();
        getLocation();
        return mFuture;
    }

    /**
     * Get the users current location with FusedLocationProviderClient and then pass the received location info to openweathermap
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    mRestInterface = WeatherApplication.initRestClient();
                    Call<WeatherModel> call = mRestInterface.getWeatherDataByLatLon(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), Constants.API_KEY);
                    call.enqueue(new Callback<WeatherModel>() {
                        @Override
                        public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                            final WeatherModel weatherModel = response.body();
                            if (weatherModel != null) {
                                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        WeatherDatabase mDb = WeatherDatabase.getInstance(getApplicationContext());
                                        mDb.daoAccess().insertOnlySingleRecord(weatherModel);
                                        mFuture.set(Result.success());
                                    }
                                });
                            } else {
                                mFuture.set(Result.failure());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeatherModel> call, Throwable t) {
                            mFuture.set(Result.failure());
                        }
                    });
                } else {
                    mFuture.set(Result.failure());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                mFuture.set(Result.failure());
            }
        });


    }
}
