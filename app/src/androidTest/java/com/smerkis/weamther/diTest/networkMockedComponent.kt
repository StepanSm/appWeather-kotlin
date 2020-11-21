package com.smerkis.weamther.diTest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smerkis.weamther.api.ApiFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

fun networkMockedComponent(mockApi: String) = module {

    single { provideOkHttpClient() }
    single { gson() }
    single { provideGsonConverterFactory(get()) }
    single { provideApiFactory(get(), get(), mockApi, mockApi) }

}
private fun provideOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient.Builder().apply {
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