package com.example.foad.sillyweather.ui.weather

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import com.example.foad.sillyweather.BuildConfig
import com.example.foad.sillyweather.R
import kotlinx.android.synthetic.main.current_weather_item.view.*
import kotlinx.android.synthetic.main.forecast_weather_item.view.*

class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var weatherListViewModel: WeatherListViewModel? = null
        set(value) {
            field = value
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
                weatherListViewModel?.currentWeatherListViewModel?.let { holder.bind(it) }
            }
            is ForecastWeatherVH -> {
                weatherListViewModel?.forecastWeatherListViewModels?.get(position - 1)?.let { holder.bind(it) }
            }
        }
    }

    inner class CurrentWeatherVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(listViewModel: WeatherListViewModel.CurrentWeatherListViewModel) {
            with(itemView) {
                tv_current_temp.text = listViewModel.temp
                tv_current_city.text = listViewModel.city
                tv_current_date.text = listViewModel.date
                tv_current_description.text = listViewModel.description
                iv_current_icon.setImageResource(listViewModel.iconResId)
            }
        }
    }

    inner class ForecastWeatherVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(listViewModel: WeatherListViewModel.ForecastWeatherListViewModel) {
            with(itemView) {
                tv_forecast_min.text = listViewModel.tempMin
                tv_forecast_max.text = listViewModel.tempMax
                tv_forecast_description.text = listViewModel.descriptiopn
                tv_forecast_date.text = listViewModel.date
                iv_forecast_icon.setImageResource(listViewModel.iconResId)
            }
        }
    }

}