package com.example.foad.sillyweather.api

import android.arch.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.Type

class LiveDataCallAdapterFactory : Factory() {
    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}