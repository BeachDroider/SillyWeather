package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.BaseDataClass
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.data.PickedCity
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import com.example.foad.sillyweather.util.util
import kotlinx.coroutines.experimental.*
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
        val application: Application,
        private val service: OpenWeatherMapService,
        private val currentWeatherDao: CurrentWeatherResponseDao,
        private val forecastWeatherDao: ForecastWeatherResponseDao) {

    lateinit var currentWeather: MutableLiveData<Resource<CurrentWeatherResponse>>
    lateinit var forecastWeather: MutableLiveData<Resource<ForecastWeatherResponseWrapper>>

    fun load(city: PickedCity) {

        currentWeather = MutableLiveData()
        forecastWeather = MutableLiveData()
        process(city.name, currentWeather, service.getCurrentWeather(city.lat, city.lng), currentWeatherDao::insert, currentWeatherDao::getCurrentWeather)
        process(city.name, forecastWeather, service.getForecastWeather(city.name), forecastWeatherDao::insert, forecastWeatherDao::getForecastWeather)

    }


    private fun <T> process(city: String,
                            livedata: MutableLiveData<Resource<T>>,
                            call: Call<T?>,
                            insert: (T) -> Any,
                            get: (String) -> T?) {

        fun <T> postError(livedata: MutableLiveData<Resource<T>>, message: String) {
            livedata.postValue(Resource.Error(null, message))
        }

        livedata.value = Resource.Loading(null)
        launch(CommonPool) {
            val initialDbResult = get(city)
            if (util.isCacheValid((initialDbResult as? (BaseDataClass))?.getTimestampForDao())) {
                livedata.postValue(Resource.Success(initialDbResult))
            } else {
                try {
                    val response = call.execute()
                    if (response.isSuccessful) {
                        response.body()?.let { insert(it) }
                        livedata.postValue(Resource.Success(get(city)))
                    } else {
                        postError(livedata, response.errorBody().toString())
                    }
                } catch (e: Exception) {
                    postError(livedata, e.toString())
                }
            }
        }
    }
}