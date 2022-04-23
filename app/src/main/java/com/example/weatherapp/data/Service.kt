package com.example.weatherapp.data

import com.example.mobilechallengeone.di.API_KEY
import com.example.weatherapp.data.models.WeatherResponse
import com.example.weatherapp.data.models.response.FullWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("q")city: String,
        @Query("appid")apiKey: String = API_KEY
    ): Response<WeatherResponse>

    @GET("onecall")
    suspend fun getAllWeatherData(
        @Query("lat")lat: Double,
        @Query("lon")lon: Double,
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("appid")apiKey: String = API_KEY
    ) : Response<FullWeatherResponse>

}
