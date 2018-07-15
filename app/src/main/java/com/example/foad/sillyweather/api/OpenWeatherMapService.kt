package com.example.foad.sillyweather.api

import com.example.foad.sillyweather.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import retrofit2.Call


interface OpenWeatherMapService {


    @GET("/data/2.5/weather")
    fun getCurrentWeather(
            @Query ("q") query: String,
            @Query("units") unit:String = "metric",
            @Query("APPID") appId: String = BuildConfig.API_KEY
            ): Call<CurrentWeatherResponse?>

    @GET("/data/2.5/forecast/daily")
    fun getForecastWeather(
            @Query("q") query: String,
            @Query("units") unit:String = "metric",
            @Query("cnt") count: Int = BuildConfig.FORECAST_ITEM_COUNT,
            @Query("APPID") appId: String = BuildConfig.API_KEY

    ): Call<ForecastWeatherResponseWrapper?>

    @GET("/data/2.5/weather")
    fun getCurrentWeather(
            @Query ("lat") lat: Double,
            @Query ("lon") lng: Double,
            @Query("units") unit:String = "metric",
            @Query("APPID") appId: String = BuildConfig.API_KEY
    ): Call<CurrentWeatherResponse?>

    @GET("/data/2.5/forecast/daily")
    fun getForecastWeather(
            @Query("lat") lat: Double,
            @Query("lon") lng: Double,
            @Query("units") unit:String = "metric",
            @Query("cnt") count: Int = BuildConfig.FORECAST_ITEM_COUNT,
            @Query("APPID") appId: String = BuildConfig.API_KEY

    ): Call<ForecastWeatherResponseWrapper?>

}