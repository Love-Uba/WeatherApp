package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weatherfordays_table")
data class WeatherForDaysEntity(
    @PrimaryKey
    val name :  String
)


