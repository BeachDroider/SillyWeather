package com.example.foad.sillyweather.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

@Dao
interface ForecastWeatherResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecastWeatherResponseWrapper: ForecastWeatherResponseWrapper)

    @Query("SELECT * FROM forecastWeatherResponseWrappers WHERE name = :city")
    fun getForecastWeather(city: String) : LiveData<ForecastWeatherResponseWrapper>

}