package com.smerkis.weamther.repository.forecast

import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.model.ApiForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForecastRepoImpl(private val apiFactory: ApiFactory) : ForecastRepo{

    override suspend fun downloadForecast(city: String) = flow {
        apiFactory.getWeatherApi().getWeatherForecast(city).let {
            emit(it)
        }
    }
}