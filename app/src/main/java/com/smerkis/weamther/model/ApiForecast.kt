package com.smerkis.weamther.model

data class ApiForecast(
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: List<Forecast>,
    val city: City
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String
)

data class Forecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val sys: Sys,
    val dt_txt: String
)

