package com.example.foad.sillyweather.di

import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

import com.example.foad.sillyweather.constants.Constants
import com.example.foad.sillyweather.ui.weather.WeatherViewModel

import javax.inject.Named

import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @FragmentScope
    @Provides
    @Named("city")
    fun getBundle(fragment: Fragment): String? {
        return fragment.arguments?.getString(Constants.WEATHER_FRAGMENT_KEY_CITY)
    }

    @FragmentScope
    @Provides
    fun provideFactory(application: Application, @Named("city") city: String?): WeatherViewModel.Factory {
          return WeatherViewModel.Factory(application, city)

    }
}
