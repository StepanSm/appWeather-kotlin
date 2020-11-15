package com.smerkis.weamther.repository.image

import android.graphics.Bitmap
import android.os.Environment
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.model.FlickrResponse
import com.smerkis.weamther.repository.BaseRepo
import io.paperdb.Paper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.*

private const val BOOK_IMAGES = "book_image"

class PaperImageRepo(val apiFactory: ApiFactory) : BaseRepo(), ImageRepo {

    override suspend fun getPhotoListFromFlickr(city: String) = flow<FlickrResponse> {
        emit(apiFactory.getImageApi().getImages(city))
    }

    override suspend fun getRandomPhotoUrl(city: String) = flow {

        val photos = apiFactory.getImageApi().getImages(city)
        if (photos.photos.photo.isNotEmpty()) {
            emit(getUrlFromPhotos(photos))
        }
    }.catch {
    }

    override suspend fun writeToCache(bitmap: Bitmap, city: String) =
        getFlow {
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

            Paper.book(BOOK_IMAGES).write(city.toLowerCase(Locale.getDefault()).trim(), imageFile)
            true
        }


    override suspend fun getImageFileFromCache(city: String) =
        getFlow {
            Paper.book(BOOK_IMAGES).read<File?>(city.toLowerCase(Locale.getDefault()).trim())
        }



    private fun String.toUUID() = UUID.nameUUIDFromBytes(this.toByteArray()).toString()

    private fun random(from: Int, to: Int) = (Math.random() * (to - from) + from).toInt()

    private fun getUrlFromPhotos(p: FlickrResponse) =
        p.photos.photo[random(0, p.photos.photo.size)].url_m
}