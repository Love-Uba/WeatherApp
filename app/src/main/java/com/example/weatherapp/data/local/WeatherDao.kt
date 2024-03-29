package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllWeather(weatherEntity: List<WeatherForDaysEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_table ORDER BY id DESC LIMIT 1")
    fun fetchWeather(): Flow<WeatherEntity>

    @Query("SELECT * FROM weatherfordays_table")
    fun fetchAllWeather(): Flow<List<WeatherForDaysEntity>>

}