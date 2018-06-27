package com.example.foad.sillyweather.di

import android.app.Application
import android.arch.persistence.room.Room
import com.example.foad.sillyweather.BuildConfig
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import com.example.foad.sillyweather.db.SillyWeatherDb
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapService(): OpenWeatherMapService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //  .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(OpenWeatherMapService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(application: Application): SillyWeatherDb {
        return Room
                .databaseBuilder(application, SillyWeatherDb::class.java, BuildConfig.DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(sillyWeatherDb: SillyWeatherDb): CurrentWeatherResponseDao {
        return sillyWeatherDb.currentWeatherDao()
    }

    @Singleton
    @Provides
    fun provideForecastWeatherDao(sillyWeatherDb: SillyWeatherDb): ForecastWeatherResponseDao {
        return sillyWeatherDb.forecastWeatherDao()
    }

}