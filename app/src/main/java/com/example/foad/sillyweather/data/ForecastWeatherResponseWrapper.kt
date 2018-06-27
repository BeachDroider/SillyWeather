package com.example.foad.sillyweather.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity

@Entity(tableName = "forecastWeatherResponseWrappers", primaryKeys =["name"])

data class ForecastWeatherResponseWrapper(
        @Embedded val city: City,
        val message: Double,
        val list: List<ForecastWeatherResponse>
)
