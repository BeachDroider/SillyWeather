package com.example.foad.sillyweather.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity


@Entity(tableName = "currentWeatherResponseDbObjects", primaryKeys = ["lon", "lat"])
data class CurrentWeatherDbObj(val lat: Double,
                               val lon: Double,
                               val timeStamp: Long,
                               @Embedded val data: CurrentWeatherResponse)


