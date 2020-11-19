package com.smerkis.weamther.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.components.IMAGE_URL
import com.smerkis.weamther.components.WEATHER_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 10L

object NetworkModule {


    val networkModule get() = apiFactoryModule

    private val apiFactoryModule = module {
        single { provideOkHttpClient() }
        single { gson() }
        single { provideGsonConverterFactory(get()) }
        single { provideApiFactory(get(), get(), WEATHER_URL, IMAGE_URL) }
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC


        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }

    private fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    private fun gson(): Gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()


    private fun provideApiFactory(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        weatherUrl: String,
        imageApi: String
    ): ApiFactory {
        return ApiFactory(client, gsonConverterFactory, weatherUrl, imageApi)
    }

}