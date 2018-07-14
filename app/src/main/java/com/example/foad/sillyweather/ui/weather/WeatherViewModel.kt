package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import android.content.Context
import android.util.Log
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.data.PickedCity

class WeatherViewModel(application: Application, val weatherRepository: WeatherRepository) : AndroidViewModel(application) {

    private val currentWeatherResource: MutableLiveData<Resource<CurrentWeatherResponse>> = weatherRepository.currentWeather
    private val forecastWeatherResource: MutableLiveData<Resource<ForecastWeatherResponseWrapper>> = weatherRepository.forecastWeather

    val weatherListViewModel: MutableLiveData<WeatherListViewModel> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    init {

        weatherListViewModel.value = WeatherListViewModel(getApplication() as Context)

        currentWeatherResource.observeForever {
            Log.i("8888", it?.data?.toString() ?: "null")
            val tempWeatherListViewModel = weatherListViewModel.value
            tempWeatherListViewModel?.currentWeatherResponse = it?.data
            weatherListViewModel.value = tempWeatherListViewModel
            updateStatus()
        }

        forecastWeatherResource.observeForever {
            val tempWeatherListViewModel = weatherListViewModel.value
            tempWeatherListViewModel?.forecastWeatherResponseWrapper = it?.data
            weatherListViewModel.value = tempWeatherListViewModel
            updateStatus()
        }

    }
    fun load(city: PickedCity) {
        weatherListViewModel.value = WeatherListViewModel(getApplication() as Context)
        weatherRepository.load(city)

    }

    private fun updateStatus() {
        error.value = currentWeatherResource.value is Resource.Error || forecastWeatherResource.value is Resource.Error
        loading.value = (currentWeatherResource.value is Resource.Loading) || (forecastWeatherResource.value is Resource.Loading)
    }

    class Factory(val application: Application, val weatherRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, weatherRepository) as T
        }
    }
}