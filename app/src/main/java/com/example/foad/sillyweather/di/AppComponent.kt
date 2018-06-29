package com.example.foad.sillyweather.di

import android.app.Application
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import com.example.foad.sillyweather.db.SillyWeatherDb
import com.example.foad.sillyweather.ui.weather.WeatherFragment
import com.example.foad.sillyweather.ui.weather.WeatherRepository
import com.example.foad.sillyweather.ui.weather.WeatherViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AppModule::class
        ]
)
interface AppComponent {
    fun application(): Application
    fun openWeatherMapService(): OpenWeatherMapService
    fun sillyWeatherDb(): SillyWeatherDb
    fun forecastWeatherDao(): ForecastWeatherResponseDao
    fun currentWeatherDao(): CurrentWeatherResponseDao

    fun inject(weatherViewModel: WeatherViewModel)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder


    }

    fun inject(weatherRepository: WeatherRepository)


}