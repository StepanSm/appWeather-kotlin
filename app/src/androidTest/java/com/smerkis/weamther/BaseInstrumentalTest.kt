package com.smerkis.weamther

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentalTest : KoinTest {

    protected val TAG = (this::class.java.simpleName + "TAG")
    protected lateinit var webServer: MockWebServer

    @Before
    open fun setup() {
        stopKoin()
        Log.d(TAG, "start setup")
        webServer = MockWebServer()
        webServer.start()
        Log.d(TAG, "start end")
    }

    @After
    open fun tearDown() {
        webServer.shutdown()
        stopKoin()
    }

    fun getResponse(json: String): MockResponse {
        return MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(json)
    }

    abstract fun createResponse(): String


    protected fun mockResponse(responseCode: Int = HttpURLConnection.HTTP_OK) {
        webServer.enqueue(MockResponse().setResponseCode(responseCode).setBody(createResponse()))
    }
}
