package com.smerkis.weamther

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.components.API_KEY
import com.smerkis.weamther.di.DaggerTestComponent
import com.smerkis.weamther.di.TestComponent
import com.smerkis.weamther.di.modules.ApiFactoryModule
import com.smerkis.weamther.di.modules.AppModule
import com.smerkis.weamther.model.WeatherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class ModuleInstrumentalTest {

    companion object {
        const val TEST_CITY = "Kurgan"
        const val TEST_ID = 78
        const val TEST_TEMPERATURE = 8.0
        const val TAG = "ModuleInstrumentalTest"

        private lateinit var webServer: MockWebServer

        @BeforeClass
        @JvmStatic
        fun setup() {
            Log.d(TAG, "start setup")
            webServer = MockWebServer()
            webServer.start()
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            webServer.shutdown()
        }
    }


    @Inject
    lateinit var app: MyApp

    @Inject
    lateinit var apiFactory: ApiFactory

    @Before
    fun before() {

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

    @After
    fun after() {
    }

    @Test
    fun appName() {
        Assert.assertEquals(app.appName, "weaMther")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun weatherRepositoryTest() {

        val resp = createWeatherResponse()
        webServer.enqueue(resp)

        runBlocking {
            val result: Flow<WeatherInfo> =
                flowOf(apiFactory.getWeatherApi().getWeather(TEST_CITY, API_KEY))
                    .flowOn(Dispatchers.IO)
            Assert.assertEquals(result.toList().size, 1)
            Assert.assertEquals(result.toList()[0].name, TEST_CITY)
            Assert.assertEquals(result.toList()[0].weather[0].id, TEST_ID)
        }
    }

    private fun createWeatherResponse(): MockResponse {
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


    fun getResponse(json: String): MockResponse {
        return MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(json)
    }
}
