package com.smerkis.weamther.di.modules


import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.components.WEATHER_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiFactoryModule {

    @Singleton
    @Provides
    fun apiFactory(
        @Named("okhttp_logging") client: OkHttpClient,
        @Named("gson") gsonConverterFactory: GsonConverterFactory,
        @Named("weather_url") weatherUrl: String
    ): ApiFactory = ApiFactory(client, gsonConverterFactory, weatherUrl)


    @Provides
    @Named("okhttp_logging")
    fun getOkHTTPClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttpLogger ", message)
            }
        })
        client.addInterceptor(logging)
        return client.build()
    }

    @Provides
    @Named("gson")
    fun getGsonConvertFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    fun gosn(): Gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()

    @Provides
    @Named("weather_url")
    fun weatherUrl(): String = WEATHER_URL
}