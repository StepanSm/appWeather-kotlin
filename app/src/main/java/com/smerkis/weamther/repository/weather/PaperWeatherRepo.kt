package com.smerkis.weamther.repository.weather

import com.smerkis.weamther.MyApp
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.isNetworkAvailable
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.BaseRepo
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.collections.HashMap

private const val BOOK_CITY = "book_city"
private const val BOOK_WEATHER = "book_weather"
private const val PAGE_CITY = "page_city"
private const val PAGE_WEATHER = "page_weather"

@FlowPreview
class PaperWeatherRepo(private val apiFactory: ApiFactory) : BaseRepo(), WeatherRepo {

    override suspend fun loadWeather(city: String): Flow<WeatherInfo> {

        return if (isNetworkAvailable(MyApp.instance)) {
            val fromServer =
                apiFactory.getWeatherApi().getWeather(city.toLowerCase(Locale.ROOT).trim())

            saveWeather(fromServer)
            saveCity(fromServer.name)
            loadCacheWeather(fromServer.name)
        } else {
            loadCacheWeather(city)
        }
    }

    override suspend fun saveCity(city: String) {
        Paper.book(BOOK_CITY).write(PAGE_CITY, city.trim())
    }

    override suspend fun saveWeather(weather: WeatherInfo) {
        val history = getHistory()
        history[weather.name.trim()] = weather
        Paper.book(BOOK_WEATHER).write(PAGE_WEATHER, history)
    }

    override suspend fun loadCity(): Flow<String> =
        flow {
            Paper.book(
                BOOK_CITY
            ).read(PAGE_CITY, "Moscow")?.let {
                emit(it)
            }
        }

    private suspend fun loadCacheWeather(city: String) = flow {
        getHistory()[city.trim()]?.let {
            emit(it)
        }
    }

    override suspend fun loadWeatherHistory(): Flow<HashMap<String, WeatherInfo>> {
        return flow { emit(getHistory()) }
    }

    override suspend fun deleteItemHistory(city: String): Flow<HashMap<String, WeatherInfo>> {
        val history = getHistory()
        history.remove(city)
        Paper.book(BOOK_WEATHER).write(PAGE_WEATHER, history)
        return flow { emit(getHistory()) }
    }

    private fun getHistory(): HashMap<String, WeatherInfo> =
        Paper.book(BOOK_WEATHER).read(
            PAGE_WEATHER, HashMap()
        )
}