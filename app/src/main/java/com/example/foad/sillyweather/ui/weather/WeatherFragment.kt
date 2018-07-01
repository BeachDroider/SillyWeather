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
import com.example.foad.sillyweather.constants.Constants.Companion.WEATHER_FRAGMENT_KEY_CITY
import kotlinx.android.synthetic.main.fragment_weather.*
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.Snackbar
import android.util.Log
import com.example.foad.sillyweather.constants.Constants
import com.example.foad.sillyweather.di.DaggerAppComponent
import javax.inject.Inject


class WeatherFragment : Fragment() {

    var city: String? = null
    var isShowingError = false
    lateinit var errorSnackbar: Snackbar

    lateinit var adapter: WeatherAdapter
    lateinit var weatherViewModelFactory: WeatherViewModel.Factory
    @Inject
    lateinit var weatherRepository: WeatherRepository
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
        errorSnackbar = Snackbar.make(sw_layout, "Error loading weather!", Snackbar.LENGTH_LONG)

        activity?.application?.let {
            DaggerAppComponent.builder().application(it).build().inject(this)
        }

        city = arguments?.getString(Constants.WEATHER_FRAGMENT_KEY_CITY)

        adapter = WeatherAdapter()
        rv_weather.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        rv_weather.layoutManager = LinearLayoutManager(activity)
        rv_weather.adapter = adapter

        sw_layout.setOnRefreshListener {
            isShowingError = false
            errorSnackbar.dismiss()
            Log.i("7777", "called snqckbar.dismiss()")

            viewModel?.refresh()
        }

        loadWeather()
    }

    fun showError() {
        if (!isShowingError){
            isShowingError = true
            errorSnackbar.show()
            Log.i("7777", "called snqckbar.show()")
        }

    }

    fun loadWeather() {

        activity?.application?.let {
            weatherViewModelFactory = WeatherViewModel.Factory(it, city, weatherRepository)
        }

        viewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel::class.java)

        viewModel?.weatherListViewModel?.observe(this, Observer {
            adapter.weatherListViewModel = it
        })

        viewModel?.loading?.observe(this, Observer {
            it?.let { sw_layout.isRefreshing = it }
        })

        viewModel?.error?.observe(this, Observer {
            it?.let {
                Log.i("9999", "error: $it")
                if (it) showError()
            }
        })


    }
}