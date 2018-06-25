package com.example.foad.sillyweather.data

data class ForecastWeatherResponse(
        val dt: Long,
        val temp: Temp,
        val pressure: Double,
        val humidity: Double,
        val weather: List<Weather>,
        val speed: Double,
        val deg: Double,
        val cloud: Double
)