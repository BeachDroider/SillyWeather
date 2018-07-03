package com.example.foad.sillyweather.data

interface BaseDataClass {

    fun getTimestampForDao(): Long
    fun setTimestampForDao(daoTimestamp: Long)
}