package com.example.weatherapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.models.response.Daily
import com.example.weatherapp.databinding.LayoutWeekdaysItemBinding
import kotlinx.android.synthetic.main.layout_weekdays_item.view.*

class WeatherForDaysAdapter : RecyclerView.Adapter<WeatherForDaysAdapter.BaseViewHolder>() {

    var adapterList = emptyList<Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bnd = LayoutWeekdaysItemBinding.inflate(layoutInflater, parent, false)
        return BaseViewHolder(bnd)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(adapterList[position])
    }

    override fun getItemCount(): Int {
        return adapterList.count()
    }

    inner class BaseViewHolder(private val bnd: LayoutWeekdaysItemBinding) :
        RecyclerView.ViewHolder(bnd.root) {
        fun bind(item: Daily) {

            item.run {
//                val iconState = getWeatherCondition(item)
                for (items in item.weather.indices) {
                    itemView.weather_summaryTv.text = item.weather[items].description
                    val icon = item.weather[items].icon
                    Glide.with(itemView.context)
                        .load("http://openweathermap.org/img/wn/$icon@2x.png")
                        .into(bnd.weatherIv)
                    itemView.temp_rangeTv.text = "${item.temp.min}/${item.temp.max}"
                    itemView.days_Tv.text = item.temp.day.toString()
                }
            }

        }
    }

//    private fun getWeatherCondition(data: Daily): String {
//        var iconCondition = "800"
//        for (count in data.weather.indices) {
//            iconCondition = when (data.weather[count].id) {
//                800 -> "800"
//                in 200..299 -> "2xx"
//                in 300..399 -> "3xx"
//                in 500..599 -> "5xx"
//                in 600..699 -> "6xx"
//                in 700..799 -> "7xx"
//                in 801..804 -> "80x"
//                else -> "2xx"
//            }
//        }
//        return iconCondition
//    }

    fun populatePredictions(weatherEntity: List<Daily>) {
        this.adapterList = weatherEntity
        notifyDataSetChanged()
    }
}