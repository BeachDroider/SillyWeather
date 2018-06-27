package com.example.foad.sillyweather.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

@Database(entities = [CurrentWeatherResponse::class, ForecastWeatherResponseWrapper::class], version = 1)
@TypeConverters(com.example.foad.sillyweather.db.TypeConverters::class)
abstract class SillyWeatherDb : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherResponseDao
    abstract fun forecastWeatherDao(): ForecastWeatherResponseDao
}