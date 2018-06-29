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
import com.example.foad.sillyweather.di.DaggerAppComponent
import com.example.foad.sillyweather.util.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository(application: Application, val city: String?) {

    @Inject lateinit var service: OpenWeatherMapService
    @Inject lateinit var currentWeatherDao: CurrentWeatherResponseDao
    @Inject lateinit var forecastDao: ForecastWeatherResponseDao


    var currentWeather: MutableLiveData<Resource<CurrentWeatherResponse>> = MutableLiveData()
    var forecastWeather: MutableLiveData<ForecastWeatherResponseWrapper> = MutableLiveData()

    init {
        DaggerAppComponent.builder().application(application).build().inject(this)

        Log.i("9999", "repo init")
        loadCurrentFromDatabase()
    }

    fun loadCurrentFromDatabase() {
        Log.i("9999", "loading From database")
        city?.let {
            val initialDbResult = currentWeatherDao.getCurrentWeather(city)
            if (util.isCacheValid(initialDbResult?.timestamp)) {
                currentWeather.value = Resource.Loading(initialDbResult)
            } else {
                loadCurrentFromNetwork()
            }
        }
    }

    private fun writeCurrentToDatabase(response: CurrentWeatherResponse?) {
        Log.i("9999", "writing current to database")

        response?.let {
            currentWeatherDao.insert(it)

            city?.let {
                Log.i("9999", "posting updated database read")
                currentWeather.value = Resource.Success(currentWeatherDao.getCurrentWeather(it))
            }
        }

    }

    private fun loadCurrentFromNetwork() {
        Log.i("9999", "loading From network")

        city?.let {
            service.getCurrentWeather(it).enqueue(
                    object : Callback<CurrentWeatherResponse> {
                        override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable?) {

                        }

                        override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse>) {
                            Log.i("9999", "onresponse")
                            val currentWeatherResponse = response.body()
                            if (response.isSuccessful) {
                                Log.i("9999", "is successful")
                                writeCurrentToDatabase(currentWeatherResponse)
                            } else {

                            }

                        }
                    }
            )
        }
    }

    fun loadForecastFromNetwork() {
        city?.let {
            service.getForecastWeather(it).enqueue(
                    object : Callback<ForecastWeatherResponseWrapper> {
                        override fun onResponse(call: Call<ForecastWeatherResponseWrapper>?, response: Response<ForecastWeatherResponseWrapper>?) {
                            val forecastWeatherResponse = response?.body()
                            forecastWeather?.value = forecastWeatherResponse
//                            bar(forecastWeatherResponse)
                        }

                        override fun onFailure(call: Call<ForecastWeatherResponseWrapper>?, t: Throwable?) {
                        }
                    }
            )
        }
    }

//    fun foo(currentWeatherResponse: CurrentWeatherResponse?) {
//        currentWeatherResponse?.let {
//            currentWeatherDao.insert(it)
//
//        }
//        city?.let {
//            currentWeatherDao.getCurrentWeather(it).let { livedata ->
//                livedata.observeForever(
//                        object : Observer<CurrentWeatherResponse> {
//                            override fun onChanged(t: CurrentWeatherResponse?) {
//                                Log.i("9999", t.toString())
//                                livedata.removeObserver(this)
//                            }
//                        }
//                )
//            }
//
//        }
//
//    }

//    fun bar(forecastWeatherResponseWrapper: ForecastWeatherResponseWrapper?) {
//        forecastWeatherResponseWrapper?.let {
//            forecastWeatherDao.insert(it)
//        }
//        city?.let {
//            forecastWeatherDao.getForecastWeather(it).let { livedata ->
//
//                livedata.observeForever(
//                        object : Observer<ForecastWeatherResponseWrapper> {
//                            override fun onChanged(t: ForecastWeatherResponseWrapper?) {
//                                Log.i("9999", t.toString())
//                                livedata.removeObserver(this)
//                            }
//                        }
//                )
//            }
//
//        }
//
//    }
}