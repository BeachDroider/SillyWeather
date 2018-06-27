package com.example.foad.sillyweather.db;

import android.arch.persistence.room.TypeConverter;

import com.example.foad.sillyweather.data.ForecastWeatherResponse;
import com.example.foad.sillyweather.data.Weather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TypeConverters {

    @TypeConverter
    public static String convertFromWeather(List<Weather> weathers){
        Weather weather = weathers.get(0);
        return Integer.toString(weather.getId()) + "," + weather.getMain() + "," + weather.getDescription() + "," + weather.getIcon();
    }

    @TypeConverter
    public static List<Weather> convertToWeather(String string){
        String[] result = string.split("\\s*,\\s*");
        ArrayList<Weather> list = new ArrayList();
        list.add(new Weather(Integer.parseInt(result[0]), result[1], result[2], result[3]));
        return list;
    }

    @TypeConverter
    public static String convertFromForecastList(List<ForecastWeatherResponse> list){
        Gson gson = new Gson();
        Type type = new TypeToken<List<ForecastWeatherResponse>>() {
        }.getType();
        String json = gson.toJson(list, type);
        return json;

    }

    @TypeConverter
    public List<ForecastWeatherResponse> convertToForecastList(String jsonString){
        Gson gson = new Gson();
        Type type = new TypeToken<List<ForecastWeatherResponse>>() {
        }.getType();
        List<ForecastWeatherResponse> countryLangList = gson.fromJson(jsonString, type);
        return countryLangList;
    }
}
