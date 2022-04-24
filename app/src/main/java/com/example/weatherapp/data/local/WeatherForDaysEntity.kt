package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherfordays_table")
data class WeatherForDaysEntity(
    @PrimaryKey
    val date : Int,
    val icon : String,
    val min : Double,
    val max: Double,
    val main: String,
    val description: String
)


