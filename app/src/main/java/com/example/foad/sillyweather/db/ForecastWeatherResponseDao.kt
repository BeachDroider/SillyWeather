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

    @Query("SELECT * FROM forecastWeatherResponseWrappers WHERE (lat/:lat > 0.99 AND lat/:lat <1.01)  AND (lon/:lon > 0.99 AND lon/:lon <1.01)")
    fun getForecastWeather(lat: Double, lon: Double) : ForecastWeatherResponseWrapper?

}