package com.example.weatherapp.data

import android.util.Log
import androidx.appcompat.view.menu.ListMenuItemView
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.models.response.FullWeatherResponse
import com.example.weatherapp.data.wrapper.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val weatherDatabase: WeatherDatabase
) {

    suspend fun getAllWeather(lat: Double, long: Double): Flow<Result<WeatherEntity>> {

        var weatherData : Flow<Result<WeatherEntity>> = weatherDatabase.weatherDao().fetchWeather().map {
            Result.Success(it)
        }

        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllWeatherData(lat, long)

                if (response.isSuccessful) {
                    weatherDatabase.weatherDao().insertWeather(DataMapper().mapper(response.body()!!))
                } else {
                    Result.Error("Failed to fetch Weather data. Try again")
                    weatherData = flow {
                        emit(Result.Error(response.message()))
                    }
                }

            } catch (ex: Exception) {
                Log.d("ERRORTAGCATCH", "catch: ${ex.localizedMessage} ")

                Result.Error(ex.localizedMessage ?: "Something went wrong.")
            }
        }
        return weatherData
    }

     fun getSavedWeather() : Flow<Result<WeatherEntity>>{

        return weatherDatabase.weatherDao().fetchWeather().map {
            Result.Success(it)
        }
    }

}