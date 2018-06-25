package com.example.foad.sillyweather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.ui.weather.WeatherAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var service: OpenWeatherMapService

    val adapter = WeatherAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as SillyWeatherApplication).appComponent!!.inject(this)

        rv_weather.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL ))
        rv_weather.adapter = adapter
        rv_weather.layoutManager = LinearLayoutManager(this)


        service.getCurrentWeather("babol").enqueue(
                object : Callback<CurrentWeatherResponse>{
                    override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse>?) {
                        Log.i("9999", call?.request()?.url().toString() ?: "sldfjsdlk")
                        adapter.currentWeatherData = response?.body()

                    }
                }
        )

        service.getForecastWeather("babol").enqueue(
                object : Callback<ForecastWeatherResponseWrapper>{
                    override fun onResponse(call: Call<ForecastWeatherResponseWrapper>?, response: Response<ForecastWeatherResponseWrapper>?) {
                        Log.i("9999", call?.request()?.url().toString())
//                        Log.i("9999", response?.body()?.forecastWeatherResponse?.get(0)?.weather?.get(0)?.description)
                        Log.i("9999", response?.body()?.list?.get(0)?.weather?.get(0)?.description)
                        adapter.forecastWeatherData = response?.body()
                    }

                    override fun onFailure(call: Call<ForecastWeatherResponseWrapper>?, t: Throwable?) {
                        Log.i("9999", t?.localizedMessage)
                    }
                }
        )
    }
}
