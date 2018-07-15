package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.*
import com.example.foad.sillyweather.db.CurrentWeatherDao
import com.example.foad.sillyweather.db.ForecastWeatherDao
import com.example.foad.sillyweather.util.util
import kotlinx.coroutines.experimental.*
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
        val application: Application,
        private val service: OpenWeatherMapService,
        private val currentWeatherDao: CurrentWeatherDao,
        private val forecastWeatherDao: ForecastWeatherDao
) {

    lateinit var currentWeather: MutableLiveData<Resource<CurrentWeatherResponse>>
    lateinit var forecastWeather: MutableLiveData<Resource<ForecastWeatherResponseWrapper>>

    fun load(city: PickedCity) {

        currentWeather = MutableLiveData()
        forecastWeather = MutableLiveData()
        loadCurrent(city)
        loadForecast(city)

    }


    private fun loadForecast(city: PickedCity){

        forecastWeather.value = Resource.Loading(null)
        launch(CommonPool) {
            val initialDbResult = forecastWeatherDao.getForecastWeather(city.lat, city.lng)
            if (util.isCacheValid(initialDbResult?.timeStamp)) {
                forecastWeather.postValue(Resource.Success(initialDbResult?.data))
            } else {
                try {
                    val response = service.getForecastWeather(city.lat, city.lng).execute()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            forecastWeatherDao.insert(ForecastWeatherDbObj(city.lat, city.lng, System.currentTimeMillis(), it))
                        }
                        val dbResult = forecastWeatherDao.getForecastWeather(city.lat, city.lng)
                        forecastWeather.postValue(Resource.Success(dbResult?.data))
                    } else {
                        postError(forecastWeather, response.errorBody().toString())
                    }
                } catch (e: Exception) {
                    postError(forecastWeather, e.toString())
                }
            }
        }
    }


    fun <T> postError(livedata: MutableLiveData<Resource<T>>, message: String) {
        livedata.postValue(Resource.Error(null, message))
    }

    private fun  loadCurrent(city: PickedCity) {



        currentWeather.value = Resource.Loading(null)
        launch(CommonPool) {
            val initialDbResult = currentWeatherDao.getCurrentWeather(city.lat, city.lng)
            if (util.isCacheValid(initialDbResult?.timeStamp)) {
                currentWeather.postValue(Resource.Success(initialDbResult?.data))
            } else {
                try {
                    val response = service.getCurrentWeather(city.lat, city.lng).execute()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            currentWeatherDao.insert(CurrentWeatherDbObj(city.lat, city.lng, System.currentTimeMillis(), it))
                        }
                        val dbResult = currentWeatherDao.getCurrentWeather(city.lat, city.lng)
                        currentWeather.postValue(Resource.Success(dbResult?.data))
                    } else {
                        postError(currentWeather, response.errorBody().toString())
                    }
                } catch (e: Exception) {
                    postError(currentWeather, e.toString())
                }
            }
        }
    }
}