package com.smerkis.weamther.api

import com.smerkis.weamther.components.KEY_WEATHER
import com.smerkis.weamther.model.ApiForecast
import com.smerkis.weamther.model.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?units=metric&appid=$KEY_WEATHER")
    suspend fun getWeather(
        @Query("q") city: String,
    ): WeatherInfo


    @GET("forecast?units=metric&appid=$KEY_WEATHER")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
    ): ApiForecast
}