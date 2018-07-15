package com.example.foad.sillyweather.data

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters

@Entity(tableName = "currentWeatherResponses", primaryKeys = ["lon", "lat"])
data class CurrentWeatherResponse(
        var timestamp: Long,
        @Embedded
        val coord: Coord,
        val weather: List<Weather>,
        @Embedded
        val main: Main,
        val visibility: Int,
        @Embedded
        val wind: Wind,
        val dt: Long,
        val name: String


) : BaseDataClass {

    override fun getTimestampForDao(): Long {
        return timestamp
    }

    override fun setTimestampForDao(daoTimestamp: Long) {
        timestamp = daoTimestamp
    }
}
