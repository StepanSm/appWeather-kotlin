package com.smerkis.weamther.repository.weather

import com.smerkis.weamther.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {

    suspend fun saveCity(city: String): Flow<Boolean>
    suspend fun loadCity(): Flow<String>

    suspend fun saveWeather(city: String, weather: WeatherInfo): Flow<Boolean>
    suspend fun loadWeather(city: String): Flow<WeatherInfo?>

    suspend fun downloadWeather(city: String): Flow<WeatherInfo>
    suspend fun loadWeatherHistory(): Flow<HashMap<String, WeatherInfo>>

}