package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.local.WeatherForDaysEntity
import com.example.weatherapp.databinding.LayoutWeekdaysItemBinding
import com.example.weatherapp.utils.UtilConstants.DATE_DISPLAY_FORMAT
import com.example.weatherapp.utils.getFormattedDateTime
import kotlinx.android.synthetic.main.layout_weekdays_item.view.*

class WeatherForDaysAdapter : RecyclerView.Adapter<WeatherForDaysAdapter.BaseViewHolder>() {

    var adapterList = emptyList<WeatherForDaysEntity>()

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
        fun bind(item: WeatherForDaysEntity) {

            item.run {
                itemView.weather_summaryTv.text = "${item.main}"
                val icon = item.icon
                Glide.with(itemView.context)
                    .load("https://openweathermap.org/img/wn/$icon@2x.png")
                    .into(bnd.weatherIv)
                itemView.temp_rangeTv.text = "${item.min}/${item.max}"
                itemView.days_Tv.text = item.date.toString().getFormattedDateTime(DATE_DISPLAY_FORMAT)
            }

        }
    }

    fun populatePredictions(weatherEntity: List<WeatherForDaysEntity>) {
        this.adapterList = weatherEntity
        notifyDataSetChanged()
    }
}