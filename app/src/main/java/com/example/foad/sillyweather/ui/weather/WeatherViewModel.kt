package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.*
import android.util.Log
import com.example.foad.sillyweather.SillyWeatherApplication
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherViewModel(application: Application, val city: String?) : AndroidViewModel(application) {

    @Inject
    lateinit var service: OpenWeatherMapService

    @Inject
    lateinit var currentWeatherDao: CurrentWeatherResponseDao

    @Inject
    lateinit var forecastWeatherDao: ForecastWeatherResponseDao

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
                            val forecastWeatherResponse = response?.body()
                            forecastWeather?.value =forecastWeatherResponse
                            bar(forecastWeatherResponse)
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
                            val currentWeatherResponse = response?.body()
                            currentWeather?.value = currentWeatherResponse
                            foo(currentWeatherResponse)

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

    fun foo(currentWeatherResponse: CurrentWeatherResponse?){
        currentWeatherResponse?.let {
            currentWeatherDao.insert(it)

        }
        city?.let {
            currentWeatherDao.getCurrentWeather(it).observeForever(Observer {
                Log.i("9999", currentWeatherResponse.toString())
            })
        }

    }

    fun bar(forecastWeatherResponseWrapper: ForecastWeatherResponseWrapper?){
        forecastWeatherResponseWrapper?.let {
            forecastWeatherDao.insert(it)
        }
        city?.let {
            forecastWeatherDao.getForecastWeather(it).observeForever(Observer {
                Log.i("9999", forecastWeatherResponseWrapper.toString())
            })
        }

    }
}