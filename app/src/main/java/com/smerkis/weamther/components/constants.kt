package com.smerkis.weamther.components

import com.smerkis.weamther.BuildConfig
import com.smerkis.weamther.R


const val KEY_WEATHER = BuildConfig.KEY_WEATHER
const val KEY_FLICKR = BuildConfig.KEY_FLICKR
const val WEATHER_URL = "http://api.openweathermap.org/data/2.5/"
const val IMAGE_URL = "https://www.flickr.com/services/rest/?"
const val IMAGE_SIZE = "url_m"
const val IMAGE_COUNT = 50
const val IMAGE_FOLDER_NAME = "saved_image"

val WEATHER_ICON = mapOf(
    "01d" to R.drawable._01d,
    "01n" to R.drawable._01n,
    "02d" to R.drawable._02d,
    "02n" to R.drawable._02n,
    "03d" to R.drawable._03d,
    "03n" to R.drawable._03n,
    "04d" to R.drawable._04d,
    "04n" to R.drawable._04n,
    "09d" to R.drawable._09d,
    "09n" to R.drawable._09n,
    "10d" to R.drawable._10d,
    "10n" to R.drawable._10n,
    "11d" to R.drawable._11d,
    "11n" to R.drawable._11n,
    "13d" to R.drawable._13d,
    "13n" to R.drawable._13n,
    "50d" to R.drawable._50d,
    "50n" to R.drawable._50n,
)