package com.example.foad.sillyweather.data

import android.arch.persistence.room.Entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "pickedCities", primaryKeys =["name", "lat", "lng"])
@Parcelize
data class PickedCity(
        val name: String,
        val lat: Double,
        val lng: Double
) : Parcelable