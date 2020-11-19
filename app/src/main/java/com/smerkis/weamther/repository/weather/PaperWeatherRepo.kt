package com.smerkis.weamther.repository.weather

import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.BaseRepo
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import java.util.*
import kotlin.collections.HashMap

private const val BOOK_CITY = "book_city"
private const val BOOK_WEATHER = "book_weather"
private const val PAGE_CITY = "page_city"
private const val PAGE_WEATHER = "page_weather"

@FlowPreview
class PaperWeatherRepo(private val apiFactory: ApiFactory) : BaseRepo(), WeatherRepo {

    override suspend fun getForecast(city: String) = flow {
        apiFactory.getWeatherApi().getWeatherForecast(city).let {
            emit(it)
        }
    }

    override suspend fun saveCity(city: String): Flow<Boolean> = flow {
        Paper.book(BOOK_CITY).write(PAGE_CITY, city.toLowerCase(Locale.ROOT).trim())?.let {
            emit(true)
        }
    }

    override suspend fun loadCity(): Flow<String> =
        flow {
            Paper.book(BOOK_CITY).read(PAGE_CITY, "Kurgan")?.let {
                emit(it)
            }
        }

    override suspend fun loadLastWeather() = flow {
        loadCity().flatMapConcat { city ->
            loadWeather(city).mapNotNull {
                emit(it)
            }
        }
    }

    override suspend fun saveWeather(city: String, weather: WeatherInfo): Flow<Boolean> =
        flow {
            val history = getHistory()
            history[city.toLowerCase(Locale.ROOT).trim()] = weather
            Paper.book(BOOK_WEATHER).write(PAGE_WEATHER, history)?.let {
                emit(true)
            }
        }

    override suspend fun loadWeather(city: String): Flow<WeatherInfo?> =
        flow {
            getHistory()[city.toLowerCase(Locale.ROOT).trim()]?.let {
                emit(it)
            }
        }

    override suspend fun downloadWeather(city: String) = flow {
        apiFactory.getWeatherApi().getWeather(city.toLowerCase(Locale.ROOT).trim())
            .let {
                emit(it)
            }
    }

    override suspend fun loadWeatherHistory(): Flow<HashMap<String, WeatherInfo>> =
        flow { getHistory() }

    private fun getHistory(): HashMap<String, WeatherInfo> =
        Paper.book(BOOK_WEATHER).read(
            PAGE_WEATHER, HashMap()
        )
}