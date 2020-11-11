package com.smerkis.weamther.repository

import com.smerkis.weamther.BuildConfig.API_KEY
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.model.WeatherInfo
import io.paperdb.Paper
import kotlinx.coroutines.flow.Flow
import java.util.*
import kotlin.collections.HashMap

private const val BOOK_CITY = "book_city"
private const val BOOK_WEATHER = "book_weather"
private const val PAGE_CITY = "page_city"
private const val PAGE_WEATHER = "page_weather"

class PaperWeatherRepo(val apiFactory: ApiFactory) : BaseRepo(), WeatherRepo {

    override fun saveCity(city: String): Flow<Boolean> =
        getFlow {
            Paper.book(BOOK_CITY).write(PAGE_CITY, city.toLowerCase(Locale.ROOT).trim())
            true
        }

    override fun loadCity(): Flow<String> =
        getFlow {
            Paper.book(BOOK_CITY).read<String>(PAGE_CITY, "Kurgan")
        }

    override fun saveWeather(city: String, weather: WeatherInfo): Flow<Boolean> =
        getFlow {
            val history = getHistory()
            history[city.toLowerCase(Locale.ROOT).trim()] = weather
            Paper.book(BOOK_WEATHER).write(PAGE_WEATHER, history)
            true
        }


    override fun loadWeather(city: String): Flow<WeatherInfo?> =
        getFlow {
            val history = getHistory()
            history[city.toLowerCase(Locale.ROOT).trim()]
        }

    override fun downloadWeather(city: String): Flow<WeatherInfo> {
        return apiFactory.getWeatherApi().getWeather(city.toLowerCase(Locale.ROOT).trim(), API_KEY)
    }

    override fun loadWeatherHistory(): Flow<HashMap<String, WeatherInfo>> =
        getFlow { getHistory() }

    private fun getHistory(): HashMap<String, WeatherInfo> =
        Paper.book(BOOK_WEATHER).read(
            PAGE_WEATHER, HashMap()
        )
}