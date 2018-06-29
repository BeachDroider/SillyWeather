package com.example.foad.sillyweather.ui.weather

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class WeatherRepository @Inject constructor(val service: OpenWeatherMapService
                                            , val currentWeatherDao: CurrentWeatherResponseDao
                                            , val forecastWeatherDao: ForecastWeatherResponseDao
                                            , @Named("city") val city: String?) {

    var currentWeather: MutableLiveData<CurrentWeatherResponse>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                loadCurrent()
            }
            return field
        }

    var forecastWeather: MutableLiveData<ForecastWeatherResponseWrapper>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
                loadForecast()
            }
            return field
        }

    fun loadForecast() {
        city?.let {
            service.getForecastWeather(it).enqueue(
                    object : Callback<ForecastWeatherResponseWrapper> {
                        override fun onResponse(call: Call<ForecastWeatherResponseWrapper>?, response: Response<ForecastWeatherResponseWrapper>?) {
                            val forecastWeatherResponse = response?.body()
                            forecastWeather?.value = forecastWeatherResponse
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

    fun foo(currentWeatherResponse: CurrentWeatherResponse?) {
        currentWeatherResponse?.let {
            currentWeatherDao.insert(it)

        }
        city?.let {
            currentWeatherDao.getCurrentWeather(it).let { livedata ->
                livedata.observeForever(
                        object : Observer<CurrentWeatherResponse> {
                            override fun onChanged(t: CurrentWeatherResponse?) {
                                Log.i("9999", t.toString())
                                livedata.removeObserver(this)
                            }
                        }
                )
            }

        }

    }

    fun bar(forecastWeatherResponseWrapper: ForecastWeatherResponseWrapper?) {
        forecastWeatherResponseWrapper?.let {
            forecastWeatherDao.insert(it)
        }
        city?.let {
            forecastWeatherDao.getForecastWeather(it).let { livedata ->

                livedata.observeForever(
                        object : Observer<ForecastWeatherResponseWrapper> {
                            override fun onChanged(t: ForecastWeatherResponseWrapper?) {
                                Log.i("9999", t.toString())
                                livedata.removeObserver(this)
                            }
                        }
                )
            }

        }

    }
}