package com.smerkis.weamther.model

data class FlickrResponse(
    val photos: Photos,
    val stat: String
)

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int,
    val url_m: String,
    val height_m: Int,
    val width_m: Int
)

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: String,
    val photo: List<Photo>
)