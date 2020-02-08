package com.intelegencia.weatherforecast.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.intelegencia.weatherforecast.service.models.WeatherModel;

@Dao
public interface WeatherDaoAccess {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleRecord(WeatherModel weatherModel);

    @Query("SELECT * FROM WeatherModel")
    WeatherModel fetchAllData();

    @Update
    void updateRecord(WeatherModel weatherModel);

}
