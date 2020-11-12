package com.smerkis.weamther.api

import com.smerkis.weamther.model.WeatherInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?units=metric")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") key: String
    ):Response<WeatherInfo>
}