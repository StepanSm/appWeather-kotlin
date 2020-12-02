package com.smerkis.weamther

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.diTest.appModule
import com.smerkis.weamther.diTest.networkMockedComponent
import com.smerkis.weamther.diTest.weatherRepoMockedModule
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import retrofit2.HttpException
import java.net.HttpURLConnection

const val TEST_CITY = "New-York"
const val TEST_ID = 78
const val TEST_TEMPERATURE = 59.0

@FlowPreview
@RunWith(AndroidJUnit4::class)
class WeatherRepoInstrumentalTest : BaseInstrumentalTest() {

    private val app: MyApp by inject()
    private val apiFactory: ApiFactory by inject()

    private val weatherRepo: WeatherRepo by inject()
    lateinit var testWeather: WeatherInfo

    override fun setup() {
        super.setup()
        startKoin {
            modules(
                listOf(
                    appModule(),
                    networkMockedComponent(webServer.url("/").toString()),
                    weatherRepoMockedModule()
                )
            )
        }
    }

    @Test
    fun appName() {
        assertEquals(app.appName, "weaMther")
    }

    @Test
    fun weather_api_test() {
        mockResponse()
        runBlocking {
            val result = apiFactory.getWeatherApi().getWeather(TEST_CITY)
            assertEquals(result.name, TEST_CITY)
            assertEquals(result.weather[0].id, TEST_ID)
        }
    }

    @Test
    fun save_city_load_city_ok() {
        mockResponse()
        runBlocking {
            weatherRepo.saveCity(TEST_CITY)
            weatherRepo.loadCity().collect {
                assertNotNull(it)
                assertEquals(it, TEST_CITY)
            }
        }
    }

    @Test
    fun load_weather_Ok() {
        mockResponse()
        runBlocking {
            weatherRepo.loadWeather(TEST_CITY).collect { weatherInfo ->
                if (weatherInfo != null) {
                    assertEquals(weatherInfo.name, TEST_CITY)
                    assertEquals(weatherInfo.weather[0].id, TEST_ID)
                    assertEquals(weatherInfo.main.temp, TEST_TEMPERATURE, 0.0)
                } else {
                    throw NullPointerException()
                }
            }
        }
    }

    @Test
    fun save_weather_load_weather_history_Ok() {
        mockResponse()
        runBlocking {

            var records = 0
            weatherRepo.deleteItemHistory(TEST_CITY)

            weatherRepo.loadWeatherHistory().collect {
                assertNull(it[TEST_CITY])
                records = it.size
            }

            testWeather = apiFactory.getWeatherApi().getWeather(TEST_CITY)

            weatherRepo.saveWeather(testWeather)

            weatherRepo.loadWeatherHistory().collect {
                assertNotNull(it)
                assertNotNull(it[TEST_CITY])
                assertEquals(it[TEST_CITY], testWeather)
                assertEquals(it.size, records + 1)
            }

            weatherRepo.deleteItemHistory(TEST_CITY)
            weatherRepo.loadWeatherHistory().collect {
                assertNull(it[TEST_CITY])
                assertEquals(it.size, records)
            }
        }
    }

    @Test(expected = HttpException::class)
    fun download_weather_error() {
        mockResponse(HttpURLConnection.HTTP_BAD_REQUEST)
        runBlocking {
            weatherRepo.loadWeather(TEST_CITY).take(1).collect {
                assertEquals(it?.weather.isNullOrEmpty(), true)
            }
        }
    }


    override fun createResponse() =
        """{
    "coord": {
        "lon": 65.33,
        "lat": 55.45
    },
    "weather": [
        {
            "id": $TEST_ID,
            "main": "Clouds",
            "description": "overcast clouds",
            "icon": "04d"
        }
    ],
    "base": "stations",
    "main": {
        "temp": $TEST_TEMPERATURE,
        "feels_like": -8.14,
        "temp_min": -1.88,
        "temp_max": -1.88,
        "pressure": 1004,
        "humidity": 93,
        "sea_level": 1004,
        "grnd_level": 995
    },
    "visibility": 10000,
    "wind": {
        "speed": 5.56,
        "deg": 319
    },
    "clouds": {
        "all": 97
    },
    "dt": 1605081944,
    "sys": {
        "country": "RU",
        "sunrise": 1605063894,
        "sunset": 1605094841
    },
    "timezone": 18000,
    "id": 1501321,
    "name": "$TEST_CITY",
    "cod": 200
}"""
}
