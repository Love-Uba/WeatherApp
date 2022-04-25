package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.local.WeatherForDaysEntity
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.models.response.FullWeatherResponse
import com.example.weatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import com.example.weatherapp.data.wrapper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _searchByCoordResponse = MutableLiveData<Result<WeatherEntity>>()
    private val _searchByWeekResponse = MutableLiveData<Result<List<WeatherForDaysEntity>>>()

    val todayFetchResponse: LiveData<Result<WeatherEntity>> = _searchByCoordResponse
    val weeklyFetchResponse: LiveData<Result<List<WeatherForDaysEntity>>> = _searchByWeekResponse

    fun actionSearch(lat: Double,long: Double) {
        _searchByCoordResponse.value = Result.Loading
        viewModelScope.launch(IO) {
            try{
                weatherRepository.getAllWeather(lat, long).collect {
                    _searchByCoordResponse.postValue(it)
                }
            }catch (ex: Exception){
                println(ex.localizedMessage)
            }
        }
    }

    fun getWeekReport(){
        _searchByWeekResponse.value = Result.Loading
        viewModelScope.launch(IO){
            try{
                weatherRepository.getAllWeekWeather().collect{
                    _searchByWeekResponse.postValue(it)
                }
            }catch(ex: Exception){
                println(ex.localizedMessage)
            }
        }
    }


}