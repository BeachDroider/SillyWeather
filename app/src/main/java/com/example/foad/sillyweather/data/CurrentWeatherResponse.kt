package com.example.foad.sillyweather.data

data class CurrentWeatherResponse(
        val weather: List<Weather>,
        val main: Main,
        val visibility: Int,
        val wind: Wind,
        val dt: Long,
        val name: String

)
