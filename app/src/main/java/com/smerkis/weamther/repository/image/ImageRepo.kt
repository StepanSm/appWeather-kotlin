package com.smerkis.weamther.repository.image

import android.graphics.Bitmap
import com.smerkis.weamther.model.FlickrResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageRepo {
    suspend fun getPhotoListFromFlickr(city: String): Flow<FlickrResponse?>
    suspend fun getRandomPhotoUrl(city: String): Flow<String?>
    suspend fun writeToCache(bitmap: Bitmap, city: String): Flow<Boolean>
    suspend fun getImageFileFromCache(city: String): Flow<File?>
    suspend fun downloadImage(city: String): Flow<Bitmap>
}