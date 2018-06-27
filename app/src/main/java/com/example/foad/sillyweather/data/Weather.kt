package com.example.foad.sillyweather.data

import android.arch.persistence.room.Entity

data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
)


