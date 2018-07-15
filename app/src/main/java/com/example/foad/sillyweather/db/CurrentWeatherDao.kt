package com.example.foad.sillyweather.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.foad.sillyweather.data.CurrentWeatherDbObj

@Dao
interface CurrentWeatherDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currenWeatherDbObj: CurrentWeatherDbObj)

    @Query("SELECT * FROM currentWeatherResponseDbObjects WHERE lat = :lat AND lon = :lon")
    fun getCurrentWeather(lat: Double, lon: Double) : CurrentWeatherDbObj?

}
