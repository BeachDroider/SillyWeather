package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import com.example.foad.sillyweather.SillyWeatherApplication
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherViewModel(application: Application, val city: String?) : AndroidViewModel(application) {

    @Inject
    lateinit var service: OpenWeatherMapService

    init {
        (getApplication<SillyWeatherApplication>()).appComponent!!.inject(this)
    }

    private var currentWeather: MutableLiveData<CurrentWeatherResponse>? = null
    private var forecastWeather: MutableLiveData<ForecastWeatherResponseWrapper>? = null

    fun getCurrentWeather(): LiveData<CurrentWeatherResponse>? {

        if (currentWeather == null) {
            currentWeather = MutableLiveData()
            loadCurrent()
        }

        return currentWeather
    }

    fun getForecastWeather(): LiveData<ForecastWeatherResponseWrapper>? {

        if (forecastWeather == null) {
            forecastWeather = MutableLiveData()
            loadForecast()
        }

        return forecastWeather
    }

    fun loadForecast() {
        city?.let {
            service.getForecastWeather(it).enqueue(
                    object : Callback<ForecastWeatherResponseWrapper> {
                        override fun onResponse(call: Call<ForecastWeatherResponseWrapper>?, response: Response<ForecastWeatherResponseWrapper>?) {
                            forecastWeather?.value = response?.body()
                        }

                        override fun onFailure(call: Call<ForecastWeatherResponseWrapper>?, t: Throwable?) {
                        }
                    }
            )
        }
    }

    fun loadCurrent() {
        city?.let {
            service.getCurrentWeather(it).enqueue(
                    object : Callback<CurrentWeatherResponse> {
                        override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable?) {

                        }

                        override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse>?) {
                            currentWeather?.value = response?.body()
                        }
                    }
            )
        }
    }

    class Factory(val application: Application, val city: String?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, city) as T
        }
    }
}