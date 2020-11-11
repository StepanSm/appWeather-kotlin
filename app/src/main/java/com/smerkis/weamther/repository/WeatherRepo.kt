package com.smerkis.weamther.repository

import com.smerkis.weamther.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {

    fun saveCity(city: String): Flow<Boolean>
    fun loadCity(): Flow<String>

    fun saveWeather(city: String, weather: WeatherInfo): Flow<Boolean>
    fun loadWeather(city: String): Flow<WeatherInfo?>

    fun downloadWeather(city: String): Flow<WeatherInfo>
    fun loadWeatherHistory(): Flow<HashMap<String, WeatherInfo>>

}