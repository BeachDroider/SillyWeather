package com.example.foad.sillyweather.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.foad.sillyweather.data.*

@Database(entities = [CurrentWeatherDbObj::class, ForecastWeatherDbObj::class, PickedCity::class], version = 1)
@TypeConverters(com.example.foad.sillyweather.db.TypeConverters::class)
abstract class SillyWeatherDb : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
    abstract fun pickedCityDao(): PickedCityDao
}