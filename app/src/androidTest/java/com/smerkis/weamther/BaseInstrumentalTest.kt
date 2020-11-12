package com.smerkis.weamther

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
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

    abstract fun createResponse(): MockResponse


    protected fun mockResponse() {
        webServer.enqueue(createResponse())
    }
}
