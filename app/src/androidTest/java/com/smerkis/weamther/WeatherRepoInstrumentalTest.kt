package com.smerkis.weamther

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.components.KEY_WEATHER
import com.smerkis.weamther.di.AppModule
import com.smerkis.weamther.di.DaggerTestComponent
import com.smerkis.weamther.di.TestComponent
import com.smerkis.weamther.di.modules.ApiFactoryModule
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.Result
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.ClassCastException
import java.lang.Error
import javax.inject.Inject

const val TEST_CITY = "Kurgan"
const val TEST_ID = 78
const val TEST_TEMPERATURE = 59.0

@RunWith(AndroidJUnit4::class)
class WeatherRepoInstrumentalTest : BaseInstrumentalTest() {
    @Inject
    lateinit var app: MyApp

    @Inject
    lateinit var apiFactory: ApiFactory

    @Inject
    lateinit var weatherRepo: WeatherRepo
    lateinit var testWeather: WeatherInfo

    override fun setup() {
        super.setup()
        val apiFactoryModule: ApiFactoryModule = object : ApiFactoryModule() {
            override fun weatherUrl() = webServer.url("/").toString()
        }

        val testComponent: TestComponent =
            DaggerTestComponent.builder().appModule(AppModule(MyApp.instance))
                .apiFactoryModule(apiFactoryModule)
                .build()
        testComponent.inject(this)
    }

    @Test
    fun appName() {
        assertEquals(app.appName, "weaMther")
    }

    @Test
    fun weather_api_test() {
        mockResponse()
        runBlocking {
            val result = apiFactory.getWeatherApi().getWeather(TEST_CITY, KEY_WEATHER)
            assertEquals(result.name, TEST_CITY)
            assertEquals(result.weather[0].id, TEST_ID)
        }
    }

    @Test
    fun save_city_ok() {
        mockResponse()
        runBlocking {
            weatherRepo.saveCity(TEST_CITY).take(1)
                .collect { assertEquals((it as Result.Success).data, true) }
        }
    }

    @Test
    fun load_list_Ok() {
        mockResponse()
        runBlocking {
            weatherRepo.loadCity().take(1)
                .collect {
                    it as Result.Success
                    assertEquals(it.data, TEST_CITY.toLowerCase()) }
        }
    }

    @Test
    fun download_weather_Ok() {
        mockResponse()
        runBlocking {
            weatherRepo.downloadWeather(TEST_CITY).take(1).collect {

                val weatherInfo = (it as Result.Success).data

                assertEquals(weatherInfo.name, TEST_CITY)
                assertEquals(weatherInfo.weather[0].id, TEST_ID)
                assertEquals(weatherInfo.main.temp, TEST_TEMPERATURE, 0.0)
            }
        }
    }

    @Test
    fun save_weather_Ok() {
        mockResponse()
        runBlocking {
            testWeather = apiFactory.getWeatherApi().getWeather(TEST_CITY, KEY_WEATHER)
            weatherRepo.saveWeather(TEST_CITY, testWeather).take(1)
                .collect {
                    it as Result.Success
                    assertEquals(it.data, true) }
        }
    }

    @Test
    fun load_weather_ok() {
        mockResponse()
        runBlocking {
            testWeather = apiFactory.getWeatherApi().getWeather(TEST_CITY, KEY_WEATHER)
            weatherRepo.loadWeather(TEST_CITY).take(1).collect {

                val loadedWeather = (it as Result.Success).data

                assertEquals(testWeather.name, loadedWeather?.name)
                assertEquals(testWeather.weather[0].id, loadedWeather?.weather?.get(0)?.id)
                assertEquals(testWeather.main.temp, loadedWeather?.main?.temp)
            }
        }
    }

    @Test
    fun load_weather_history_Ok() {
        mockResponse()
        runBlocking {
            weatherRepo.loadWeatherHistory().take(1).collect {
                assertEquals(1, (it as Result.Success).data.size)
            }
        }

    }


    @Test(expected = ClassCastException::class)
    fun cce() {
        mockResponse()
        runBlocking {
            weatherRepo.downloadWeather(TEST_CITY).take(1).collect {
                it as Result.Error
            }
        }
    }

    override fun createResponse(): MockResponse {
        val json = """{
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
        return getResponse(json)
    }
}
