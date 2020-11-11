package com.smerkis.weamther.api

import com.smerkis.weamther.model.WeatherInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?units=metric")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") key: String
    ): Deferred<WeatherInfo>
}