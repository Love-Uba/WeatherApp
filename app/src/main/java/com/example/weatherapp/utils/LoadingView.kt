package com.example.weatherapp.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.weatherapp.R
import kotlinx.android.synthetic.main.layout_loading_view.view.*

class LoadingView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    init {
        inflate(context, R.layout.layout_loading_view, this)
        val ta: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.LoadingView)
        loadingTextView.text = ta.getString(R.styleable.LoadingView_loadingText)

        ta.recycle()
    }

    fun setLoadingText(text : String){
        loadingTextView.text = text
    }
}