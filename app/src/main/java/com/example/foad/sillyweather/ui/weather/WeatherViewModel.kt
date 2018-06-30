package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

class WeatherViewModel(application: Application,
                       val city: String?,
                       weatherRepository: WeatherRepository) : AndroidViewModel(application) {

    private var currentWeatherResource: MutableLiveData<Resource<CurrentWeatherResponse>>
    private var forecastWeatherResource: MutableLiveData<Resource<ForecastWeatherResponseWrapper>>
    var currentWeather: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    var forecastWeather: MutableLiveData<ForecastWeatherResponseWrapper> = MutableLiveData()
    var error: MutableLiveData<Boolean> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        weatherRepository.city = city
        error.value = false
        loading.value = false

        currentWeatherResource = weatherRepository.currentWeather
        forecastWeatherResource = weatherRepository.forecastWeather


        currentWeatherResource.observeForever {
            currentWeather.value = it?.data
            updateStatus()
        }

        forecastWeatherResource.observeForever{
            forecastWeather.value = it?.data
            updateStatus()
        }
    }

    private fun updateStatus(){
        loading.value = (currentWeatherResource.value is Resource.Loading) || (forecastWeatherResource.value is Resource.Loading)
        error.value = (currentWeatherResource.value is Resource.Error) || (forecastWeatherResource.value is Resource.Error)

    }

    class Factory(val application: Application, val city: String?, val weatherRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, city, weatherRepository) as T
        }
    }
}