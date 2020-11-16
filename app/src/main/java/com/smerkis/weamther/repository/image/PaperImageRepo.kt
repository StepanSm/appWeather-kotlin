package com.smerkis.weamther.repository.image

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.model.FlickrResponse
import com.smerkis.weamther.repository.BaseRepo
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

private const val BOOK_IMAGES = "book_image"

@FlowPreview
class PaperImageRepo(val apiFactory: ApiFactory) : BaseRepo(), ImageRepo {

    override suspend fun getPhotoListFromFlickr(city: String) = flow<FlickrResponse> {
        emit(apiFactory.getImageApi().getImages(city))
    }

    override suspend fun getRandomPhotoUrl(city: String): Flow<String> = flow {
        val photos = apiFactory.getImageApi().getImages(city)
        if (photos.photos.photo.isNotEmpty()) {
            emit(getUrlFromPhotos(photos))
        }
    }.catch {
        println()
    }

    override suspend fun writeToCache(bitmap: Bitmap, city: String) =
        flow {
            var imageFile = File(
                MyApp.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "${city.toLowerCase(Locale.getDefault()).trim().toUUID()}.jpg"
            )

            if (imageFile.exists()) {
                imageFile.delete()
                imageFile = File(
                    MyApp.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "${city.toLowerCase(Locale.getDefault()).trim().toUUID()}.jpg"
                )
            }

            FileOutputStream(imageFile).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it)
                it.flush()
            }

            val result = Paper.book(BOOK_IMAGES)
                .write(city.toLowerCase(Locale.getDefault()).trim(), imageFile)
            if (result != null) {
                emit(true)
            }
        }


    override suspend fun getImageFileFromCache(city: String) =
        flow {
            val image =
                Paper.book(BOOK_IMAGES).read<File?>(city.toLowerCase(Locale.getDefault()).trim())
            emit(image)
        }.filterNotNull()

    override suspend fun downloadImage(city: String): Flow<Bitmap> {
        Log.d("SplashViewModel", "onWeatherUpdated downloadImage")
        return getRandomPhotoUrl(city).flatMapConcat { url ->
            Log.d("SplashViewModel", "onWeatherUpdated map")
            flow {
                emit(getBitmap(url))
            }
        }
    }

    private suspend fun getBitmap(url: String): Bitmap {
        val imageLoader = ImageLoader(MyApp.instance)
        val request = ImageRequest.Builder(MyApp.instance)
            .data(url).target().build()
        val result = (imageLoader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun isOnline(): Boolean {
        return true
    }

    private fun String.toUUID() = UUID.nameUUIDFromBytes(this.toByteArray()).toString()

    private fun random(from: Int, to: Int) = (Math.random() * (to - from) + from).toInt()

    private fun getUrlFromPhotos(p: FlickrResponse) =
        p.photos.photo[random(0, p.photos.photo.size)].url_m
}