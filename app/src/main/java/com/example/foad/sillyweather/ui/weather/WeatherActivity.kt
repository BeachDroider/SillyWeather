package com.example.foad.sillyweather.ui.weather

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.constants.Constants.Companion.WEATHER_ACTIVITY_KEY_CITY

import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val cityName = intent.extras.getString(WEATHER_ACTIVITY_KEY_CITY)

        if (savedInstanceState == null) {
            val weatherFragment = WeatherFragment.newInstance(cityName)
            supportFragmentManager.beginTransaction().add(R.id.container_weather, weatherFragment).commit()
        }

    }

}
