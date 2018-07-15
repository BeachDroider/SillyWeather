package com.example.foad.sillyweather.data

import android.arch.persistence.room.Embedded

data class City (
    val id: Long,
    val name: String,
    @Embedded
    val coord: Coord
)