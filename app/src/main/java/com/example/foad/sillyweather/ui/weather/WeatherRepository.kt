package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import com.example.foad.sillyweather.util.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
        val application: Application,
        val service: OpenWeatherMapService,
        val currentWeatherDao: CurrentWeatherResponseDao,
        val forecastWeatherDao: ForecastWeatherResponseDao) {

    lateinit var currentWeather: MutableLiveData<Resource<CurrentWeatherResponse>>
    lateinit var forecastWeather: MutableLiveData<Resource<ForecastWeatherResponseWrapper>>

    var city: String? = null
        set(value) {
            field = value
            currentWeather = MutableLiveData()
            forecastWeather = MutableLiveData()
            load()
        }

    fun load(){
        loadCurrentFromDatabase()
        loadForecastFromDatabase()
    }

    private fun loadCurrentFromDatabase() {
        city?.let {
            val initialDbResult = currentWeatherDao.getCurrentWeather(it)
            if (util.isCacheValid(initialDbResult?.timestamp)) {
                currentWeather.value = Resource.Success(initialDbResult)
            } else {
                currentWeather.value = Resource.Loading(null)
                loadCurrentFromNetwork()
            }
        }
    }

    private fun loadCurrentFromNetwork() {
        city?.let {
            service.getCurrentWeather(it).enqueue(
                    object : Callback<CurrentWeatherResponse> {
                        override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse?>) {
                            val currentWeatherResponse = response.body()
                            if (response.isSuccessful) {
                                writeCurrentToDatabase(currentWeatherResponse)
                            } else {
                                currentWeather.value = Resource.Error(currentWeatherResponse, response.errorBody().toString())
                            }
                        }

                        override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable?) {
                            currentWeather.value = Resource.Error(null, t?.message)
                        }
                    }
            )
        }
    }

    private fun writeCurrentToDatabase(response: CurrentWeatherResponse?) {
        // write to and read back from db for single source of truth
        response?.let {
            currentWeatherDao.insert(it)
            city?.let {
                currentWeather.value = Resource.Success(currentWeatherDao.getCurrentWeather(it))
            }
        }
    }

    private fun loadForecastFromDatabase() {
        city?.let {
            val initialDbResult = forecastWeatherDao.getForecastWeather(it)
            if (util.isCacheValid(initialDbResult?.timestamp)) {
                forecastWeather.value = Resource.Success(initialDbResult)
            } else {
                forecastWeather.value = Resource.Loading(null)
                loadForecastFromNetwork()
            }
        }
    }

    private fun loadForecastFromNetwork() {
        city?.let {
            service.getForecastWeather(it).enqueue(
                    object : Callback<ForecastWeatherResponseWrapper> {
                        override fun onResponse(call: Call<ForecastWeatherResponseWrapper>?, response: Response<ForecastWeatherResponseWrapper?>) {
                            val forecastWeatherResponseWrapper = response.body()
                            if (response.isSuccessful) {
                                writeForecastToDatabase(forecastWeatherResponseWrapper)
                            } else {
                                forecastWeather.value = Resource.Error(forecastWeatherResponseWrapper, "network error loading forecast")
                            }
                        }

                        override fun onFailure(call: Call<ForecastWeatherResponseWrapper>?, t: Throwable?) {
                            forecastWeather.value = Resource.Error(null, "no network to load forecast")
                        }
                    }
            )
        }
    }

    private fun writeForecastToDatabase(response: ForecastWeatherResponseWrapper?) {
        response?.let {
            forecastWeatherDao.insert(it)
            city?.let {
                forecastWeather.value = Resource.Success(forecastWeatherDao.getForecastWeather(it))
            }
        }
    }


}