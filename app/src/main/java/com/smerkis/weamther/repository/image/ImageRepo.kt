package com.smerkis.weamther.repository.image

import kotlinx.coroutines.flow.Flow

interface ImageRepo {

    suspend fun downloadImage(city: String): Flow<String>
}