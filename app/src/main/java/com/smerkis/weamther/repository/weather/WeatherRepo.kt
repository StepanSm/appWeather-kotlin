package com.smerkis.weamther.repository.weather

import com.smerkis.weamther.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {

    suspend fun loadCity(): Flow<String>
    suspend fun saveCity(city: String)

    suspend fun loadWeather(city: String): Flow<WeatherInfo?>
    suspend fun saveWeather(weather: WeatherInfo)

    suspend fun loadWeatherHistory(): Flow<HashMap<String, WeatherInfo>>

    suspend fun deleteItemHistory(city: String): Flow<HashMap<String, WeatherInfo>>
}
