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

    val weatherListViewModel: MutableLiveData<WeatherListViewModel> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    private var city: PickedCity? = null

    private var currentWeatherResource: MutableLiveData<Resource<CurrentWeatherResponse>>? = null
    private var forecastWeatherResource: MutableLiveData<Resource<ForecastWeatherResponseWrapper>>? = null

    private var currentObserver: Observer<Resource<CurrentWeatherResponse>>? = null
    private var forecastObserver: Observer<Resource<ForecastWeatherResponseWrapper>>? = null

    fun refresh() {
        city?.let { load(it) } ?: reset()
    }

    fun reset() {
        weatherListViewModel.value = WeatherListViewModel(getApplication() as Context)
        error.value = false
        loading.value = false
    }

    fun load(city: PickedCity) {
        this.city = city
        reset()
        currentObserver?.let { currentWeatherResource?.removeObserver(it) }
        forecastObserver?.let { forecastWeatherResource?.removeObserver(it) }

        weatherRepository.load(city)

        currentWeatherResource = weatherRepository.currentWeather
        forecastWeatherResource = weatherRepository.forecastWeather

        currentObserver = Observer {
            val tempWeatherListViewModel = weatherListViewModel.value
            tempWeatherListViewModel?.currentWeatherResponse = it?.data
            weatherListViewModel.value = tempWeatherListViewModel
            updateStatus()
        }

        forecastObserver = Observer {
            val tempWeatherListViewModel = weatherListViewModel.value
            tempWeatherListViewModel?.forecastWeatherResponseWrapper = it?.data
            weatherListViewModel.value = tempWeatherListViewModel
            updateStatus()
        }

        currentObserver?.let { currentWeatherResource?.observeForever(it) }
        forecastObserver?.let { forecastWeatherResource?.observeForever(it) }

    }

    private fun updateStatus() {
        error.value = currentWeatherResource?.value is Resource.Error || forecastWeatherResource?.value is Resource.Error
        loading.value = (currentWeatherResource?.value is Resource.Loading) || (forecastWeatherResource?.value is Resource.Loading)
    }

    class Factory(val application: Application, val weatherRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, weatherRepository) as T
        }
    }
}