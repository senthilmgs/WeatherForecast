package com.intelegencia.weatherforecast.view.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.intelegencia.weatherforecast.R;
import com.intelegencia.weatherforecast.base.BaseActivity;
import com.intelegencia.weatherforecast.db.AppExecutors;
import com.intelegencia.weatherforecast.db.WeatherDatabase;
import com.intelegencia.weatherforecast.service.models.WeatherModel;
import com.intelegencia.weatherforecast.utils.Constants;
import com.intelegencia.weatherforecast.viewmodels.WeatherViewModel;

/**
 * WeatherDetailActivity, which shows the receiveed weather information on to UI with the help of ViewModel along with RoomDatabase
 */
public class WeatherDetailActivity extends BaseActivity {

    private TextView tv_cityname;
    private TextView tv_temp;
    private TextView tv_climate_description;
    private ImageView weather_icon;
    private ProgressBar weather_progressBar;

    private WeatherViewModel weatherViewModel;
    private String TAG = WeatherDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        tv_cityname = findViewById(R.id.tv_cityname);
        tv_temp = findViewById(R.id.tv_temp);
        tv_climate_description = findViewById(R.id.tv_climate_description);
        weather_progressBar = findViewById(R.id.progress_circular);
        weather_icon = findViewById(R.id.weather_icon);

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        checkLocationPermission();
    }

    // Check the location permission and shows the dialog to enble the permission by user
    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            // Will be shown when the permission is already enabled
            getWeatherData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isNetworkAvailable()) {
                        getWeatherData();
                    } else {
                     // When device not connected with wifi, fetches the local roomdata
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                WeatherDatabase mDb = WeatherDatabase.getInstance(getApplicationContext());
                                WeatherModel weatherModel = mDb.daoAccess().fetchAllData();
                                generateUI(weatherModel);
                            }
                        });
                    }
                } else {
                    showWeatherToast("Please enable location permissions");
                    finish();
                }
                break;
        }
    }
// Register a observer to listen the ViewModel to get updates from UI
    private void getWeatherData() {
        weatherViewModel.getWeatherReport(this).observe(this, new Observer<WeatherModel>() {
            @Override
            public void onChanged(WeatherModel weatherModel) {
                if (weatherModel != null) {
                    generateUI(weatherModel);
                } else {
                    showWeatherToast("Try again later!");
                }
            }
        });
    }

    private void generateUI(WeatherModel weatherModel) {
        tv_cityname.setText(weatherModel.getName());
        tv_temp.setText(weatherModel.getMain().getTemp() + "");
        tv_climate_description.setText(weatherModel.getWeather().get(0).getMain() + ", " + weatherModel.getWeather().get(0).getDescription());
        loadImageFromUrlToImageView(weather_icon, weatherModel.getWeather().get(0).getIcon());
    }

    private void loadImageFromUrlToImageView(final ImageView imageView, String imageIconName) {

        if (imageIconName != null) {
            String imageUri = Constants.IMAGE_URL + imageIconName + ".png";
            Glide.with(getApplicationContext()).load(imageUri)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            weather_progressBar.setVisibility(View.GONE);

        } else {
            Log.e(TAG, "Image ICON NOT FOUND");
        }
    }
}
