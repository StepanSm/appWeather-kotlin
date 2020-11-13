package com.smerkis.weamther.repository.weather

import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {

    suspend fun saveCity(city: String): Flow<Result<Boolean>>
    suspend fun loadCity(): Flow<Result<String>>

    suspend fun loadLastWeather(): Flow<Result<WeatherInfo?>>
    suspend fun saveWeather(city: String, weather: WeatherInfo): Flow<Result<Boolean>>
    suspend fun loadWeather(city: String): Flow<Result<WeatherInfo?>>

    suspend fun downloadWeather(city: String): Flow<Result<WeatherInfo>>
    suspend fun loadWeatherHistory(): Flow<Result<HashMap<String, WeatherInfo>>>
}
