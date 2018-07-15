package com.example.foad.sillyweather.di

import android.app.Application
import android.arch.persistence.room.Room
import com.example.foad.sillyweather.BuildConfig
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.db.*
import com.google.gson.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapService(gson: Gson): OpenWeatherMapService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(OpenWeatherMapService::class.java)
    }

    @Singleton
    @Provides
    fun provideGson(
    ): Gson {
        return GsonBuilder()
                .create()
    }


    @Singleton
    @Provides
    fun provideDb(application: Application): SillyWeatherDb {
        return Room
                .databaseBuilder(application, SillyWeatherDb::class.java, BuildConfig.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(sillyWeatherDb: SillyWeatherDb): CurrentWeatherDao {
        return sillyWeatherDb.currentWeatherDao()
    }

    @Singleton
    @Provides
    fun provideForecastWeatherDao(sillyWeatherDb: SillyWeatherDb): ForecastWeatherDao {
        return sillyWeatherDb.forecastWeatherDao()
    }

    @Singleton
    @Provides
    fun providePickedCityDao(sillyWeatherDb: SillyWeatherDb): PickedCityDao {
        return sillyWeatherDb.pickedCityDao()
    }

}