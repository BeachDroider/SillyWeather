package com.example.foad.sillyweather.di

import android.app.Application
import com.example.foad.sillyweather.MainActivity
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

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder

//        fun apiModule(apiModule: ApiModule): Builder

    }
}