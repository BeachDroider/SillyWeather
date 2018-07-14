package com.example.foad.sillyweather.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.foad.sillyweather.data.PickedCity

@Dao
interface PickedCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pickedCity: PickedCity)

    @Query("SELECT * FROM pickedCities")
    fun getPickedCities() : List<PickedCity>
}