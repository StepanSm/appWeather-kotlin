package com.smerkis.weamther.repository.weather

import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.components.CELSIUM
import com.smerkis.weamther.components.KEY_WEATHER
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.BaseRepo
import com.smerkis.weamther.repository.Result
import io.paperdb.Paper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Error
import java.util.*
import kotlin.collections.HashMap

private const val BOOK_CITY = "book_city"
private const val BOOK_WEATHER = "book_weather"
private const val PAGE_CITY = "page_city"
private const val PAGE_WEATHER = "page_weather"

class PaperWeatherRepo(private val apiFactory: ApiFactory) : BaseRepo(), WeatherRepo {

    override suspend fun saveCity(city: String) = getFlow {
        Paper.book(BOOK_CITY).write(PAGE_CITY, city.toLowerCase(Locale.ROOT).trim())
        true
    }

    override suspend fun loadCity() =
        getFlow {
            Paper.book(BOOK_CITY).read(PAGE_CITY, "Kurgan")
        }

    override suspend fun loadLastWeather(): Flow<Result<WeatherInfo?>> {
        throw NotImplementedError("loadLastWeather wasn't Implemented")
    }

    override suspend fun saveWeather(city: String, weather: WeatherInfo) =
        getFlow {
            val history = getHistory()
            history[city.toLowerCase(Locale.ROOT).trim()] = weather
            Paper.book(BOOK_WEATHER).write(PAGE_WEATHER, history)
            true
        }


    override suspend fun loadWeather(city: String) =
        getFlow {
            val history = getHistory()
            history[city.toLowerCase(Locale.ROOT).trim()]
        }

    override suspend fun downloadWeather(city: String) = flow {
        val weather =
            apiFactory.getWeatherApi().getWeather(city.toLowerCase(Locale.ROOT).trim(), KEY_WEATHER)
        emit(Result.Success(weather))
    }.catch {
        Result.Error(it)
    }


    override suspend fun loadWeatherHistory() = getFlow { getHistory() }

    private fun getHistory(): HashMap<String, WeatherInfo> =
        Paper.book(BOOK_WEATHER).read(
            PAGE_WEATHER, HashMap()
        )
}