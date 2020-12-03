package com.smerkis.weamther.repository.image

import coil.ImageLoader
import coil.request.ImageRequest
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.isNetworkAvailable
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val BOOK_IMAGE = "book_image"
private const val PAGE_IMAGE = "page_image"


@FlowPreview
class PaperImageRepo(private val apiFactory: ApiFactory) : ImageRepo {

    override suspend fun downloadImage(city: String): Flow<String> {
        return if (isNetworkAvailable(MyApp.instance)) {
            val listImage = apiFactory.getImageApi().getImages("$city city").photos.photo
            val randomIndex = (Math.random() * listImage.size).toInt()

            caching(listImage[randomIndex].url_m)

            flow { emit(listImage[randomIndex].url_m) }
        } else {
            flow { emit(fromCache()) }
        }
    }

    private fun fromCache(): String {
        return Paper.book(BOOK_IMAGE).read(PAGE_IMAGE)
    }

    private suspend fun caching(url: String) {
        val imageLoader = ImageLoader(MyApp.instance)
        val request = ImageRequest.Builder(MyApp.instance)
            .data(url).build()
        imageLoader.execute(request)

        Paper.book(BOOK_IMAGE).write(PAGE_IMAGE, url)
    }
}