package com.example.foad.sillyweather.ui.weather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.SillyWeatherApplication
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.constants.Constants.Companion.WEATHER_FRAGMENT_KEY_CITY
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import kotlinx.android.synthetic.main.fragment_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherFragment : Fragment() {

    @Inject
    lateinit var service: OpenWeatherMapService
    lateinit var adapter: WeatherAdapter

    var city: String? = null

    companion object {
        fun newInstance(city: String?): WeatherFragment {
            val weatherFragment = WeatherFragment()
            weatherFragment.arguments = Bundle().apply { putString(WEATHER_FRAGMENT_KEY_CITY, city) }
            return weatherFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        city = arguments?.getString(WEATHER_FRAGMENT_KEY_CITY)
        (activity?.application as SillyWeatherApplication).appComponent!!.inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = WeatherAdapter()
        rv_weather.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL ))
        rv_weather.layoutManager = LinearLayoutManager(activity)
        rv_weather.adapter = adapter

        city?.let { selectCity(it) }
    }

    fun selectCity(string: String) {

        service.getCurrentWeather(string).enqueue(
                object : Callback<CurrentWeatherResponse> {
                    override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse>?) {
                        adapter.currentWeatherData = response?.body()

                    }
                }
        )

        service.getForecastWeather(string).enqueue(
                object : Callback<ForecastWeatherResponseWrapper>{
                    override fun onResponse(call: Call<ForecastWeatherResponseWrapper>?, response: Response<ForecastWeatherResponseWrapper>?) {
                        Log.i("9999", call?.request()?.url().toString())
                        adapter.forecastWeatherData = response?.body()
                    }

                    override fun onFailure(call: Call<ForecastWeatherResponseWrapper>?, t: Throwable?) {
                    }
                }
        )
    }
}