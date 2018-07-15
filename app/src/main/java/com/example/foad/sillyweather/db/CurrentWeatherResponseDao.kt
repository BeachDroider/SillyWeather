package com.example.foad.sillyweather.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.foad.sillyweather.data.CurrentWeatherResponse

@Dao
interface CurrentWeatherResponseDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currentWeather: CurrentWeatherResponse)

    @Query("SELECT * FROM currentWeatherResponses WHERE (lat/:lat > 0.95 AND lat/:lat <1.05)  AND (lon/:lon > 0.95 AND lon/:lon <1.05)")
    fun getCurrentWeather(lat: Double, lon: Double) : CurrentWeatherResponse?

}
