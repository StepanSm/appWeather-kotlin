package com.smerkis.weamther.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory(
    private val client: OkHttpClient,
    private val gsonConverterFactory: GsonConverterFactory,
    private val weatherUrl: String,
    private val imageApi: String
) {
    private inline fun <reified T> getApi(baserUrl: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baserUrl)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
        return retrofit.create(T::class.java)
    }

    fun getWeatherApi(): WeatherApi = getApi(weatherUrl)
    fun getImageApi(): ImageApi = getApi(imageApi)
}