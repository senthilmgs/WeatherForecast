package com.intelegencia.weatherforecast.service.models.common;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataConverter {

        @TypeConverter
        public String fromWeatherList(List<Weather> countryLang) {
            if (countryLang == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<List<Weather>>() {}.getType();
            String json = gson.toJson(countryLang, type);
            return json;
        }

        @TypeConverter
        public List<Weather> toWeatherList(String countryLangString) {
            if (countryLangString == null) {
                return (null);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Weather>>() {}.getType();
            List<Weather> countryLangList = gson.fromJson(countryLangString, type);
            return countryLangList;
        }
    }

