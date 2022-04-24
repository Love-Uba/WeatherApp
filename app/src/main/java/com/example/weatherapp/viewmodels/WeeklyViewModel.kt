package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.models.response.FullWeatherResponse
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {

}