package com.example.foad.sillyweather.di

import android.app.Application
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.db.CurrentWeatherDao
import com.example.foad.sillyweather.db.ForecastWeatherDao
import com.example.foad.sillyweather.db.SillyWeatherDb
import com.example.foad.sillyweather.ui.weather.MainActivity
import com.example.foad.sillyweather.ui.weather.WeatherFragment
import com.example.foad.sillyweather.ui.weather.WeatherViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun application(): Application
    fun openWeatherMapService(): OpenWeatherMapService
    fun sillyWeatherDb(): SillyWeatherDb
    fun forecastWeatherDao(): ForecastWeatherDao
    fun currentWeatherDao(): CurrentWeatherDao

    fun inject(weatherViewModel: WeatherViewModel)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder


    }

    fun inject(weatherFragment: WeatherFragment)
    fun inject(mainActivity: MainActivity)


}