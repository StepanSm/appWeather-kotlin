package com.smerkis.weamther.repository.forecast

import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.model.ApiForecast
import kotlinx.coroutines.flow.Flow

class ForecastRepoImpl(private val apiFactory: ApiFactory) : ForecastRepo{



    override suspend fun downloadForecast(city: String): Flow<ApiForecast> {
        TODO("Not yet implemented")
    }

}