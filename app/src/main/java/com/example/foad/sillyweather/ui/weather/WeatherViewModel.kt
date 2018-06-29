package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper

class WeatherViewModel(application: Application, val city: String?) : AndroidViewModel(application) {


    private var currentWeatherResource: MutableLiveData<Resource<CurrentWeatherResponse>>
    var currentWeather: MutableLiveData<CurrentWeatherResponse> = MediatorLiveData()
    var forecastWeather: LiveData<ForecastWeatherResponseWrapper>?

    init {

        val weatherRepository = WeatherRepository(application, city)

        currentWeatherResource = weatherRepository.currentWeather
        forecastWeather = weatherRepository.forecastWeather

        Log.i("9999", "viewmodel init")
        currentWeatherResource.observeForever {
            currentWeather.value = it?.data
        }

    }

    class Factory(val application: Application, val city: String?) : ViewModelProvider.NewInstanceFactory() {
        init {
            Log.i("9999", "factory init")
        }

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            Log.i("9999", "factory create")
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, city) as T
        }
    }
}