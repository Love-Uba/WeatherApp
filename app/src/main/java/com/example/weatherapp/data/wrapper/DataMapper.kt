package com.example.weatherapp.data.wrapper

import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.local.WeatherForDaysEntity
import com.example.weatherapp.data.models.response.Daily
import com.example.weatherapp.data.models.response.FullWeatherResponse

class DataMapper {

    fun mapper(weatherResponse: FullWeatherResponse): WeatherEntity {
        return WeatherEntity(
            weatherResponse.timezone,
            weatherResponse.lon,
            weatherResponse.lat,
            weatherResponse.current.dt,
            weatherResponse.current.weather[0].main,
            weatherResponse.current.weather[0].icon,
            weatherResponse.current.temp,
            weatherResponse.current.humidity,
            weatherResponse.current.wind_speed,
            weatherResponse.current.feels_like,
            weatherResponse.current.uvi
        )
    }

    fun weekMapper(dailyReport: Daily): WeatherForDaysEntity {
        return WeatherForDaysEntity(
            dailyReport.dt,
            dailyReport.weather[0].icon,
            dailyReport.temp.min,
            dailyReport.temp.max,
            dailyReport.weather[0].description,
            dailyReport.weather[0].main
        )
    }
}
