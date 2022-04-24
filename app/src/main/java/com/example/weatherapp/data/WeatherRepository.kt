package com.example.weatherapp.data

import android.util.Log
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.models.response.FullWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {

//    suspend fun getWeather(city: String): Result<WeatherResponse> {
//
//        return try {
//
//            val response = apiService.getWeatherData(city)
//
//            if (response.isSuccessful) {
//                Result.Success(response.body()!!)
//            } else {
//                Result.Error("Failed to fetch Weather data. Try again")
//            }
//        } catch (ex: Exception) {
//
//            Result.Error(ex.localizedMessage ?: "Something went wrong.")
//        }
//    }


    suspend fun getAllWeather(lat: Double,long: Double) : Result<FullWeatherResponse>{

        return try {

            val response = apiService.getAllWeatherData(lat,long)

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Failed to fetch Weather data. Try again")
            }
        } catch (ex: Exception) {
            Log.d("ERRORTAGCATCH", "catch: ${ex.localizedMessage} ")

            Result.Error(ex.localizedMessage ?: "Something went wrong.")
        }
    }

}