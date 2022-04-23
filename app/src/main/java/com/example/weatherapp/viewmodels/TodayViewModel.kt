package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import com.example.weatherapp.data.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val searchByCityResponse = SingleLiveEvent<Result<WeatherResponse>>()

    val getSearchResponse: LiveData<Result<WeatherResponse>> = searchByCityResponse

    fun actionSearch(city: String) {
        Log.d("TESTVM", "onCreateView: called")
        searchByCityResponse.value = Result.Loading
        viewModelScope.launch {
            searchByCityResponse.value = weatherRepository.getWeather(city)
        }
    }


}