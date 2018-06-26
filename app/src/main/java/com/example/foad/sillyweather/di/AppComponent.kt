package com.example.foad.sillyweather.di

import android.app.Application
import com.example.foad.sillyweather.ui.weather.MainActivity
import com.example.foad.sillyweather.ui.weather.WeatherFragment
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

//    fun inject(mainActivity: MainActivity)
    fun inject(weatherFragment: WeatherFragment)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder


    }
}