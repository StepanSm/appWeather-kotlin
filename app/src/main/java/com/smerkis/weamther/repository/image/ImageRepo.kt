package com.smerkis.weamther.repository.image

import android.graphics.Bitmap
import com.smerkis.weamther.model.image.FlickrResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageRepo {
    suspend fun getPhotoList(city: String): Flow<FlickrResponse?>
    suspend fun getRandomPhotoUrl(city: String): Flow<String?>
    suspend fun writeToCache(bitmap: Bitmap, city: String): Flow<Boolean>
    suspend fun getImageFileFromCache(city: String): Flow<File?>
}