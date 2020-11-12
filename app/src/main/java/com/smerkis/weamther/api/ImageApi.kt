package com.smerkis.weamther.api

import com.smerkis.weamther.components.IMAGE_URL
import com.smerkis.weamther.model.image.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("?safe_search=safe&format=json&content_type=1&sort=relevance&method=flickr.photos.search&media=photos&nojsoncallback=1&api_key=$IMAGE_URL&extras=uri_m&per_page=1&text=kurgan")
    fun getImages(
        @Query("text") text: String
    ): FlickrResponse
}