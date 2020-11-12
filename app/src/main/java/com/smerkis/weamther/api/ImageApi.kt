package com.smerkis.weamther.api

import com.smerkis.weamther.components.KEY_FLICKR
import com.smerkis.weamther.model.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("safe_search=safe&format=json&content_type=1&sort=relevance&method=flickr.photos.search&media=photos&nojsoncallback=1&api_key=$KEY_FLICKR&extras=uri_m&per_page=50")
    suspend fun getImages(
        @Query("text") text: String
    ): FlickrResponse
}