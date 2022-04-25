package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    val timezone :  String,
    val longitude : Double,
    val latitude : Double,
    val date : Int,
    val status : String,
    val icon : String,
    val temp : Double,
    val humidity : Int,
    val wind: Double,
    val feelsLike: Double,
    val uvIndex:Double,
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
