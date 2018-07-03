package com.example.foad.sillyweather.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity

@Entity(tableName = "forecastWeatherResponseWrappers", primaryKeys =["name"])

data class ForecastWeatherResponseWrapper(
        var timestamp: Long,
        @Embedded val city: City,
        val message: Double,
        val list: List<ForecastWeatherResponse>
) : BaseDataClass{

    override fun getTimestampForDao(): Long {
        return timestamp
    }


    override fun setTimestampForDao(daoTimestamp: Long) {
        timestamp = daoTimestamp
    }
}
