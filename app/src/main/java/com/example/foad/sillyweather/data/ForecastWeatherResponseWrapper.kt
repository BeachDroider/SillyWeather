package com.example.foad.sillyweather.data

data class ForecastWeatherResponseWrapper(
        val message: Double,
        val list: List<ForecastWeatherResponse>
)
