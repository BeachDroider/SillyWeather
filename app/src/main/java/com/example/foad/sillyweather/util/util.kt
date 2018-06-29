package com.example.foad.sillyweather.util

import com.example.foad.sillyweather.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

object util {

    fun convertEpochToString(date: Long): String {
        val format = SimpleDateFormat("EEEE, d MMM")
        return format.format(date * 1000)
    }

    fun isCacheValid(timestampMillis: Long?) : Boolean{
        return timestampMillis?.let {
            System.currentTimeMillis() - it < (BuildConfig.CACHE_TIMEOUT_SECS * 1000)
        } ?: false

    }

}