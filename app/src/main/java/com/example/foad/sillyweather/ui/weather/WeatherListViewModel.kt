package com.example.foad.sillyweather.ui.weather

import android.content.Context
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.util.util
import kotlin.math.roundToInt

class WeatherListViewModel(val context: Context,
                           currentWeatherResponse: CurrentWeatherResponse?,
                           forecastWeatherResponseWrapper: ForecastWeatherResponseWrapper?) {

    init {
        currentWeatherResponse?.let {
            currentWeatherListViewModel = CurrentWeatherListViewModel(it)

        }
        forecastWeatherResponseWrapper?.let { responseWrapper ->
            forecastWeatherListViewModels = responseWrapper.list.map { response ->
                ForecastWeatherListViewModel(response)
            }
        }
    }


    var currentWeatherListViewModel: CurrentWeatherListViewModel? = null
    var forecastWeatherListViewModels: List<ForecastWeatherListViewModel>? = null


    private fun getLocalIconResource(context: Context, iconNameOnServer: String): Int {
        val resId = context.resources.getIdentifier("ic_$iconNameOnServer", "drawable", context.packageName)
        return if (resId != 0) resId else context.resources.getIdentifier("ic_na", "drawable", context.packageName)
    }


    inner class CurrentWeatherListViewModel(val currentWeatherResponse: CurrentWeatherResponse) {
        var temp: String? = null
        var city: String? = null
        var date: String? = null
        var description: String? = null
        var iconResId: Int = 0

        init {
            with(currentWeatherResponse) {
                temp = main.temp.roundToInt().toString() + " \u2103"
                city = name
                date = util.convertEpochToString(dt)
                description = weather[0].description
                iconResId = getLocalIconResource(context, weather[0].icon)

            }
        }
    }

    inner class ForecastWeatherListViewModel(val forecastWeatherResponse: ForecastWeatherResponse) {

        var tempMin: String? = null
        var tempMax: String? = null
        var descriptiopn: String? = null
        var date: String? = null
        var iconResId: Int = 0

        init {

            with(forecastWeatherResponse) {
                tempMin = temp.min.roundToInt().toString()
                tempMax = temp.max.roundToInt().toString()
                descriptiopn = weather[0].main
                date = util.convertEpochToString(forecastWeatherResponse.dt)
                iconResId = getLocalIconResource(context, weather[0].icon)


            }

        }
    }

}