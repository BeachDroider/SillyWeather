package com.example.foad.sillyweather.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.data.PickedCity

@Database(entities = [CurrentWeatherResponse::class, ForecastWeatherResponseWrapper::class, PickedCity::class], version = 1)
@TypeConverters(com.example.foad.sillyweather.db.TypeConverters::class)
abstract class SillyWeatherDb : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherResponseDao
    abstract fun forecastWeatherDao(): ForecastWeatherResponseDao
    abstract fun pickedCityDao(): PickedCityDao
}