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
import kotlinx.android.synthetic.main.fragment_weather.*
import android.arch.lifecycle.ViewModelProviders
import android.util.Log
import com.example.foad.sillyweather.data.PickedCity
import com.example.foad.sillyweather.di.DaggerAppComponent
import javax.inject.Inject


class WeatherFragment : Fragment() {

    lateinit var adapter: WeatherAdapter
    lateinit var weatherViewModelFactory: WeatherViewModel.Factory
    @Inject
    lateinit var weatherRepository: WeatherRepository
    private var viewModel: WeatherViewModel? = null

    companion object {
        fun newInstance(): WeatherFragment {
            return WeatherFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            DaggerAppComponent.builder().application(it).build().inject(this)
        }

        adapter = WeatherAdapter()
        rv_weather.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        rv_weather.layoutManager = LinearLayoutManager(activity)
        rv_weather.adapter = adapter

//        sw_layout.setOnRefreshListener {
//            viewModel?.load(city)
//        }

        activity?.application?.let {
            weatherViewModelFactory = WeatherViewModel.Factory(it, weatherRepository)
        }

        viewModel = ViewModelProviders.of(this, weatherViewModelFactory).get(WeatherViewModel::class.java)
        observe()

    }

    fun loadWeather(city: PickedCity) {

        viewModel?.load(city)
        observe()



    }

    private fun observe(){
        viewModel?.weatherListViewModel?.observe(this, Observer {
            adapter.weatherListViewModel = it
        })

        viewModel?.loading?.observe(this, Observer {
            it?.let { sw_layout.isRefreshing = it }
        })

        viewModel?.error?.observe(this, Observer {
        })
    }

}