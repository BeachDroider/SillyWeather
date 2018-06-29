package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

class WeatherViewModel(
        application: Application,
        private val weatherRepository: WeatherRepository)
    : AndroidViewModel(application) {

    var currentWeather: LiveData<CurrentWeatherResponse>? = null
        get() {
            return weatherRepository.getCurrentWeather()
        }
    var forecastWeather: LiveData<ForecastWeatherResponseWrapper>? = null
        get() {
            return weatherRepository.getForecastWeather()
        }

    class Factory(val application: Application, val repo: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, repo) as T
        }
    }

}