package com.example.foad.sillyweather.ui.weather

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.api.Resource
import com.example.foad.sillyweather.data.BaseDataClass
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
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
        city?.let {
            process(it, currentWeather, service.getCurrentWeather(it), currentWeatherDao::insert, currentWeatherDao::getCurrentWeather)
            process(it, forecastWeather, service.getForecastWeather(it), forecastWeatherDao::insert, forecastWeatherDao::getForecastWeather)
        }
    }

    private fun <T> postError(livedata: MutableLiveData<Resource<T>>, message: String) {

        livedata.postValue(Resource.Error(null, message))

    }

    private fun <T> process(city: String,
                            livedata: MutableLiveData<Resource<T>>,
                            call: Call<T?>,
                            insert: (T) -> Any,
                            get: (String) -> T?) {

        launch(CommonPool) {
            val initialDbResult = get(city)
            if (util.isCacheValid((initialDbResult as? (BaseDataClass))?.getTimestampForDao())) {
                livedata.postValue(Resource.Success(initialDbResult))
            } else {
                livedata.postValue(Resource.Loading(null))
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