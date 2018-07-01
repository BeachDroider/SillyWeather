package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

class WeatherViewModel(application: Application,
                       val city: String?,
                       val weatherRepository: WeatherRepository) : AndroidViewModel(application) {

    private var currentWeatherResource: MutableLiveData<Resource<CurrentWeatherResponse>>
    private var forecastWeatherResource: MutableLiveData<Resource<ForecastWeatherResponseWrapper>>

    var weatherListViewModel: MutableLiveData<WeatherListViewModel> = MutableLiveData()

    var error: MutableLiveData<Boolean> = MutableLiveData()
    var forecastError: MutableLiveData<Boolean> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        weatherRepository.city = city
        error.value = false
        loading.value = false

        currentWeatherResource = weatherRepository.currentWeather
        forecastWeatherResource = weatherRepository.forecastWeather


        currentWeatherResource.observeForever {
            update()
        }

        forecastWeatherResource.observeForever {
            update()
        }
    }

    fun refresh(){
        weatherRepository.load()
    }

    private fun update() {
        weatherListViewModel.value = WeatherListViewModel(
                getApplication() as Context,
                currentWeatherResource.value?.data,
                forecastWeatherResource.value?.data)

        error.value = currentWeatherResource.value is Resource.Error || forecastWeatherResource.value is Resource.Error
        loading.value = (currentWeatherResource.value is Resource.Loading) || (forecastWeatherResource.value is Resource.Loading)


    }


    class Factory(val application: Application, val city: String?, val weatherRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, city, weatherRepository) as T
        }
    }
}