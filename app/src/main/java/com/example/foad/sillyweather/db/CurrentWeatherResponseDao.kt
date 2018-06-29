package com.example.foad.sillyweather.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.foad.sillyweather.data.CurrentWeatherResponse

@Dao
interface CurrentWeatherResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currentWeather: CurrentWeatherResponse)

    @Query("SELECT * FROM currentWeatherResponses WHERE name = :city")
    fun getCurrentWeather(city: String) : CurrentWeatherResponse?
}
