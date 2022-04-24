package com.example.weatherapp.data.wrapper

import com.example.weatherapp.data.local.WeatherEntity
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
            weatherResponse.daily[0].humidity,
            weatherResponse.daily[0].wind_speed,
            weatherResponse.current.feels_like,
            weatherResponse.daily[0].uvi
        )
    }


}
