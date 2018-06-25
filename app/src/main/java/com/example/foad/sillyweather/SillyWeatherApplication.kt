package com.example.foad.sillyweather

import android.app.Application
import com.example.foad.sillyweather.di.AppComponent
import com.example.foad.sillyweather.di.DaggerAppComponent

class SillyWeatherApplication : Application() {

    var appComponent : AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().application(this).build()
    }
}