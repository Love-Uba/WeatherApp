package com.example.mobilechallengeone.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.ApiService
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.utils.UtilConstants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun makeGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideClient(
        logger: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL).build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        var INSTANCE: WeatherDatabase? = null

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                WeatherDatabase.dbase
            ).build()

            INSTANCE = instance

            instance
        }
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }

    @Provides
    fun provideRepository(apiService: ApiService, weatherDatabase: WeatherDatabase) = WeatherRepository(apiService, weatherDatabase)

}