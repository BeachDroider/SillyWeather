package com.example.foad.sillyweather.ui.weather

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.constants.Constants.Companion.CITY_FRAGMENT_TAG
import com.example.foad.sillyweather.constants.Constants.Companion.WEATHER_ACTIVITY_KEY_CITY
import com.example.foad.sillyweather.constants.Constants.Companion.WEATHER_FRAGMENT_TAG


class MainActivity : AppCompatActivity(), CityFragment.CitySelectionListener {

    var cityFragment: CityFragment? = null
    var weatherFragment: WeatherFragment? = null

    private fun isPortrait() = (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)

    private fun initFrags() {
        cityFragment = supportFragmentManager.findFragmentByTag(CITY_FRAGMENT_TAG) as CityFragment?
        weatherFragment = supportFragmentManager.findFragmentByTag(WEATHER_FRAGMENT_TAG) as WeatherFragment?

        if (cityFragment == null) {
            cityFragment = CityFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.container_city, cityFragment, CITY_FRAGMENT_TAG).commit()
        }

        if (weatherFragment == null && !isPortrait()) {
            weatherFragment = WeatherFragment.newInstance(null)
            supportFragmentManager.beginTransaction().add(R.id.container_weather, weatherFragment, WEATHER_FRAGMENT_TAG).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFrags()
    }

    override fun onCitySelected(city: String) {
        if (isPortrait()) {
            Intent(this, WeatherActivity::class.java).apply {
                putExtra(WEATHER_ACTIVITY_KEY_CITY, city)
                startActivity(this)
            }
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.container_weather, WeatherFragment.newInstance(city), WEATHER_FRAGMENT_TAG).commit()
        }
    }


}

