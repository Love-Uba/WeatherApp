package com.example.weatherapp.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CustomWeatherDataItemBinding



class CustomWeatherData @JvmOverloads constructor(
    context: Context,
    attributesSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attributesSet, defStyleAttr) {

    private var binding: CustomWeatherDataItemBinding = CustomWeatherDataItemBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        val ta: TypedArray =
            context.obtainStyledAttributes(attributesSet, R.styleable.CustomWeatherDataItemView)
        try {
            binding.customImageView.setImageResource(
                ta.getResourceId(
                    R.styleable.CustomWeatherDataItemView_icon,
                    R.drawable.ic_thermostat
                )
            )
            binding.customTextview.text = ta.getString(R.styleable.CustomWeatherDataItemView_title)
            binding.customTempTextview.text = ta.getString(R.styleable.CustomWeatherDataItemView_subtitle)
        } finally {
            ta.recycle()
        }
    }


    fun setTempItem(subtitle:String){
        binding.customTempTextview.text = subtitle
    }

    fun setTitleItem(title:String){
        binding.customTextview.text = title
    }

    fun setItemIcon(icon:Drawable){
        binding.customImageView.setImageDrawable(icon)
    }
}