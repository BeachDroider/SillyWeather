package com.example.foad.sillyweather.ui.weather

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.SillyWeatherApplication
import com.example.foad.sillyweather.constants.Constants.Companion.WEATHER_FRAGMENT_KEY_CITY
import kotlinx.android.synthetic.main.fragment_weather.*

import android.arch.lifecycle.ViewModelProviders
import com.example.foad.sillyweather.di.DaggerFragmentComponent
import javax.inject.Inject


class WeatherFragment : Fragment() {

    lateinit var adapter: WeatherAdapter
    @Inject lateinit var weatherViewModelFactory: WeatherViewModel.Factory
    private var viewModel: WeatherViewModel? = null

    companion object {
        fun newInstance(city: String?): WeatherFragment {
            val weatherFragment = WeatherFragment()
            weatherFragment.arguments = Bundle().apply { putString(WEATHER_FRAGMENT_KEY_CITY, city) }
            return weatherFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        DaggerFragmentComponent.builder()
                .appComponent((activity?.application as SillyWeatherApplication).appComponent)
                .fragment(this)
                .build()
                .inject(this)


        adapter = WeatherAdapter()
        rv_weather.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        rv_weather.layoutManager = LinearLayoutManager(activity)
        rv_weather.adapter = adapter

        loadWeather()
    }

    fun loadWeather() {

        activity?.application?.let {
            viewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel::class.java)
        }

        viewModel?.getCurrentWeather()?.observe(this, Observer {
            adapter.currentWeatherData = it
        })

        viewModel?.getForecastWeather()?.observe(this, Observer {
            adapter.forecastWeatherData = it
        })
    }
}