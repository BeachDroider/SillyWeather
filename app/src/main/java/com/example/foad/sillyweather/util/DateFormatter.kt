package com.example.foad.sillyweather.util

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

        fun convertEpochToString(date: Long) : String{
            val format = SimpleDateFormat("EEEE, d MMM")
            return format.format(date * 1000)
    }
}