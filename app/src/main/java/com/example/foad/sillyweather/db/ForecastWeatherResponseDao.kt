package com.example.foad.sillyweather.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.foad.sillyweather.data.ForecastWeatherDbObj
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

@Dao
interface ForecastWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecastWeatherDbObj: ForecastWeatherDbObj)

    @Query("SELECT * FROM forecastWeatherResponseDbObjects WHERE lat = :lat AND lon = :lon")
    fun getForecastWeather(lat: Double, lon: Double) : ForecastWeatherDbObj?

}