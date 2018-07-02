package com.example.foad.sillyweather.di

import android.app.Application
import android.arch.persistence.room.Room
import com.example.foad.sillyweather.BuildConfig
import com.example.foad.sillyweather.api.OpenWeatherMapService
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.db.CurrentWeatherResponseDao
import com.example.foad.sillyweather.db.ForecastWeatherResponseDao
import com.example.foad.sillyweather.db.SillyWeatherDb
import com.google.gson.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapService(gson: Gson): OpenWeatherMapService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                //  .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(OpenWeatherMapService::class.java)
    }

    @Singleton
    @Provides
    fun provideGson(currentDeserializer: JsonDeserializer<CurrentWeatherResponse>, forecastDeserializer: JsonDeserializer<ForecastWeatherResponseWrapper>) : Gson{
        return GsonBuilder()
                .registerTypeAdapter(CurrentWeatherResponse::class.java, currentDeserializer)
                .registerTypeAdapter(ForecastWeatherResponseWrapper::class.java, forecastDeserializer)
                .create()
    }

    @Singleton
    @Provides
    // server returns no timestamp, so we add it locally to check against db
    fun provideCurrentDeserializer() : JsonDeserializer<CurrentWeatherResponse>{
        return object : JsonDeserializer<CurrentWeatherResponse>{
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CurrentWeatherResponse {

                json?.asJsonObject?.addProperty("timestamp", System.currentTimeMillis())
                return GsonBuilder().create().fromJson<CurrentWeatherResponse>(json, CurrentWeatherResponse::class.java)
            }
        }
    }

    @Singleton
    @Provides
    // server returns no timestamp, so we add it locally to check against db
    fun provideForecastDeserializer() : JsonDeserializer<ForecastWeatherResponseWrapper>{
        return object : JsonDeserializer<ForecastWeatherResponseWrapper>{
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ForecastWeatherResponseWrapper {

                json?.asJsonObject?.addProperty("timestamp", System.currentTimeMillis())
                return GsonBuilder().create().fromJson<ForecastWeatherResponseWrapper>(json, ForecastWeatherResponseWrapper::class.java)
            }
        }
    }

    @Singleton
    @Provides
    fun provideDb(application: Application): SillyWeatherDb {
        return Room
                .databaseBuilder(application, SillyWeatherDb::class.java, BuildConfig.DB_NAME)
                .fallbackToDestructiveMigration()
               // .allowMainThreadQueries()
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