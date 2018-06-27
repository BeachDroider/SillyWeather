package com.example.foad.sillyweather.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity


data class ForecastWeatherResponse(
        val dt: Long,
        @Embedded val temp: Temp,
        val pressure: Double,
        val humidity: Double,
        val weather: List<Weather>,
        val speed: Double,
        val deg: Double,
        val cloud: Double
)