package com.smerkis.weamther.repository.forecast

import com.smerkis.weamther.model.ApiForecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepo {

    suspend fun downloadForecast(city: String): Flow<ApiForecast>
}