package com.smerkis.weamther.model.image

data class Photo(
    val farm: Int,
    val height_m: Int,
    val id: String,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String,
    val title: String,
    val url_m: String,
    val width_m: Int
)