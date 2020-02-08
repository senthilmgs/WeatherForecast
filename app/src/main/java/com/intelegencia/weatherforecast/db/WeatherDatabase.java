package com.intelegencia.weatherforecast.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.intelegencia.weatherforecast.service.models.WeatherModel;

@Database(entities = {WeatherModel.class}, version = 2, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "weather_info";
    private static WeatherDatabase sInstance;

    public static WeatherDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        WeatherDatabase.class, WeatherDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract WeatherDaoAccess daoAccess();
}
