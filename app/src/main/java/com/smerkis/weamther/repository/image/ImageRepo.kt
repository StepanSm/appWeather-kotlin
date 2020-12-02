package com.smerkis.weamther.repository.image

import android.graphics.Bitmap
import com.smerkis.weamther.model.FlickrResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageRepo {

    suspend fun downloadImage(city: String): Flow<String>
}