package com.smerkis.weamther

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smerkis.weamther.model.WeatherInfo
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentalTest {

    protected val TAG = (this::class.java.simpleName + "TAG")
    protected lateinit var webServer: MockWebServer

    @Before
    open fun setup() {
        Log.d(TAG, "start setup")
        webServer = MockWebServer()
        webServer.start()
        Log.d(TAG, "start end")
    }

    @After
    open fun tearDown() {
        webServer.shutdown()
    }

    fun getResponse(json: String): MockResponse {
        return MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(json)
    }

    protected fun createWeatherResponse(): MockResponse {
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


    protected fun mockResponse() {
        webServer.enqueue(createWeatherResponse())
    }


}
