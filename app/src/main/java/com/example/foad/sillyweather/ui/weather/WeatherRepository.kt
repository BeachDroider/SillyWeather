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
import kotlinx.coroutines.experimental.*
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

    fun load() {
        city?.let {
            doCurrent(it)
            doForecast(it)
        }

    }

    private fun postCurrentError(message: String) {

        currentWeather.postValue(Resource.Error(null, message))

    }

    private fun doCurrent(city: String) {

        launch(CommonPool) {
            val initialDbResult = currentWeatherDao.getCurrentWeather(city)
            if (util.isCacheValid(initialDbResult?.timestamp)) {
                currentWeather.postValue(Resource.Success(initialDbResult))
            } else {
                currentWeather.postValue(Resource.Loading(null))
                try {
                    val response = service.getCurrentWeather(city).execute()
                    if (response.isSuccessful) {
                        response.body()?.let { currentWeatherDao.insert(it) }
                        currentWeather.postValue(Resource.Success(currentWeatherDao.getCurrentWeather(city)))
                    } else {
                        postCurrentError(response.errorBody().toString())
                    }
                } catch (e: Exception) {
                    postCurrentError(e.toString())
                }

            }
        }
    }

    private fun doForecast(city: String) {

        launch {
            val initialDbResult = forecastWeatherDao.getForecastWeather(city)
            if (util.isCacheValid(initialDbResult?.timestamp)) {
                forecastWeather.postValue(Resource.Success(initialDbResult))
            } else {
                forecastWeather.postValue(Resource.Loading(null))
                val response = service.getForecastWeather(city).execute()
                if (response.isSuccessful) {
                    response.body()?.let { forecastWeatherDao.insert(it) }
                    forecastWeather.postValue(Resource.Success(forecastWeatherDao.getForecastWeather(city)))
                } else {
                    Log.i("9999", "thre 1")
                    forecastWeather.postValue(Resource.Error(null, response.errorBody().toString()))
                    Log.i("9999", "thre 2")

                }
            }
        }
    }

}