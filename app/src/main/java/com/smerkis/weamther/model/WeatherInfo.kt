package com.smerkis.weamther.model

import android.os.Parcelable
import com.smerkis.weamther.getTemperature
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherInfo(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) : Parcelable

@Parcelize
data class Wind(
    val deg: Int,
    val speed: Double
) : Parcelable {
    fun getSpeed() = speed.toString()
}

@Parcelize
data class Clouds(
    val all: Int
) : Parcelable

@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
) : Parcelable

@Parcelize
data class Sys(
    val country: String,
    val sunrise: Int,
    val sunset: Int
) : Parcelable

@Parcelize
data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
) : Parcelable {

    fun getHumidity(): String {
        return "$humidity \u0025"
    }

    fun getPressure() = pressure.toString()

    fun getRange() = "min ${getTemperature(temp_min)}__max ${getTemperature(temp_max)}"
}

@Parcelize
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Parcelable