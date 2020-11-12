package com.smerkis.weamther

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.components.API_KEY
import com.smerkis.weamther.di.DaggerTestComponent
import com.smerkis.weamther.di.TestComponent
import com.smerkis.weamther.di.modules.ApiFactoryModule
import com.smerkis.weamther.di.modules.AppModule
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.WeatherRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
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
        val apiModule: ApiFactoryModule = object : ApiFactoryModule() {
            override fun weatherUrl(): String {
                return webServer.url("/").toString()
            }
        }

        val testComponent: TestComponent =
            DaggerTestComponent.builder().appModule(AppModule(MyApp.instance))
                .apiFactoryModule(apiModule)
                .build()
        testComponent.inject(this)
    }

    @Test
    fun appName() {
        assertEquals(app.appName, "weaMther")
    }

    @Test
    fun weatherApiTest() {
        mockResponse()
        runBlocking {
            val result = apiFactory.getWeatherApi().getWeather(TEST_CITY, API_KEY).body()
            assertEquals(result?.name, TEST_CITY)
            assertEquals(result?.weather?.get(0)?.id, TEST_ID)
        }
    }

    @Test
    fun saveCityOk() {
        mockResponse()
        runBlocking {
            weatherRepo.saveCity(TEST_CITY).take(1)
                .collect { assertEquals(it, true) }
        }
    }

    @Test
    fun loadCityOk() {
        mockResponse()
        runBlocking {
            weatherRepo.loadCity().take(1)
                .collect { assertEquals(it, TEST_CITY.toLowerCase()) }
        }
    }

    @Test
    fun downloadWeatherOk() {
        mockResponse()
        runBlocking {
            weatherRepo.downloadWeather(TEST_CITY).take(1).collect { weatherInfo ->
                assertEquals(weatherInfo.name, TEST_CITY)
                assertEquals(weatherInfo.weather[0].id, TEST_ID)
                assertEquals(weatherInfo.main.temp, TEST_TEMPERATURE, 0.0)
            }
        }
    }

    @Test
    fun saveWeatherOk() {
        mockResponse()
        runBlocking {
            testWeather = apiFactory.getWeatherApi().getWeather(TEST_CITY, API_KEY).body()!!
            weatherRepo.saveWeather(TEST_CITY, testWeather).take(1)
                .collect { assertEquals(it, true) }
        }
    }

    @Test
    fun loadWeatherOk() {
        mockResponse()
        runBlocking {
            testWeather = apiFactory.getWeatherApi().getWeather(TEST_CITY, API_KEY).body()!!
            weatherRepo.loadWeather(TEST_CITY).take(1).collect { loadedWeather ->
                assertEquals(testWeather.name, loadedWeather?.name)
                assertEquals(testWeather.weather[0].id, loadedWeather?.weather?.get(0)?.id)
                assertEquals(testWeather.main.temp, loadedWeather?.main?.temp)
            }
        }
    }

    @Test
    fun loadWeatherHistoryOk() {
        mockResponse()
        runBlocking {
            weatherRepo.loadWeatherHistory().take(1).collect { loadedWeather ->
                assertEquals(loadedWeather.size, 1)
            }
        }

    }
}
