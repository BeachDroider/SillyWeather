package com.example.foad.sillyweather.api

sealed class Resource<T>(val data: T?) {


    data class Success<T> (val successData: T?) : Resource<T>(successData)
    data class Error<T>(val errorData: T?, val message: String?) : Resource<T>(errorData)
    data class Loading<T>(val loadingData: T?) : Resource<T>(loadingData)
}