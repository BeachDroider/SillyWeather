package com.example.foad.sillyweather.ui.weather

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.foad.sillyweather.BuildConfig
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.data.CurrentWeatherResponse
import com.example.foad.sillyweather.data.ForecastWeatherResponseWrapper
import com.example.foad.sillyweather.util.DateFormatter
import kotlinx.android.synthetic.main.current_weather_item.view.*
import kotlinx.android.synthetic.main.forecast_weather_item.view.*
import kotlin.math.roundToInt

class WeatherAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currentWeatherData: CurrentWeatherResponse? = null
        set(data) {
            field = data
            notifyDataSetChanged()
        }

    var forecastWeatherData: ForecastWeatherResponseWrapper? = null
        set(data) {
            field = data
            notifyDataSetChanged()
        }

    enum class ViewType(val type: Int) {
        CURRENT(0), FORECAST(1)
    }

    override fun getItemCount() = BuildConfig.FORECAST_ITEM_COUNT + 1

    override fun getItemViewType(position: Int) = if (position == 0) ViewType.CURRENT.type else ViewType.FORECAST.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.CURRENT.type -> CurrentWeatherVH(LayoutInflater.from(parent.context).inflate(R.layout.current_weather_item, parent, false))
            else -> ForecastWeatherVH(LayoutInflater.from(parent.context).inflate(R.layout.forecast_weather_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CurrentWeatherVH -> {
                currentWeatherData?.let { holder.bind(it) } ?: holder.showProgress()
            }
            is ForecastWeatherVH -> {
                forecastWeatherData?.let { holder.bind(it) } ?: holder.showProgress()
            }
        }
    }

    inner class CurrentWeatherVH(view: View) : RecyclerView.ViewHolder(view) {
        fun showProgress() = itemView.pb_current.setVisibility(VISIBLE)
        fun bind(data: CurrentWeatherResponse) {
            with(itemView) {
                tv_current_temp.text = data.main.temp.roundToInt().toString() + " \u2103"
                tv_current_city.text = data.name
                tv_current_date.text = DateFormatter.convertEpochToString(data.dt)
                tv_current_description.text = data.weather[0]?.description
                iv_current_icon.setImageResource(getLocalIconResource(data.weather[0].icon))
                itemView.pb_current.visibility = GONE
            }
        }
    }

    inner class ForecastWeatherVH(view: View) : RecyclerView.ViewHolder(view) {
        fun showProgress() = itemView.pb_forecast.setVisibility(VISIBLE)
        fun bind(data: ForecastWeatherResponseWrapper) {
            with(itemView) {
                val forecastWeatherResponse = data.list[adapterPosition - 1]
                tv_forecast_min.text = forecastWeatherResponse.temp.min.roundToInt().toString()
                tv_forecast_max.text = forecastWeatherResponse.temp.max.roundToInt().toString()
                tv_forecast_description.text = forecastWeatherResponse.weather[0].main
                tv_forecast_date.text = DateFormatter.convertEpochToString(forecastWeatherResponse.dt)
                iv_forecast_icon.setImageResource(getLocalIconResource(forecastWeatherResponse.weather[0].icon))
                pb_forecast.visibility = GONE
            }
        }
    }

    private fun getLocalIconResource(iconNameOnServer: String) : Int {

        val resId = context.resources.getIdentifier("ic_$iconNameOnServer", "drawable", context.packageName)
        return if (resId != 0) resId else context.resources.getIdentifier("ic_na", "drawable", context.packageName)
    }

}