package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.models.response.FullWeatherResponse
import com.example.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import com.example.weatherapp.data.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val searchByCoordResponse = SingleLiveEvent<Result<FullWeatherResponse>>()

    val getFetchResponse: LiveData<Result<FullWeatherResponse>> = searchByCoordResponse

    fun actionSearch(lat: Double,long: Double) {
        searchByCoordResponse.value = Result.Loading
        viewModelScope.launch {
            searchByCoordResponse.value = weatherRepository.getAllWeather(lat, long)
        }
    }
}