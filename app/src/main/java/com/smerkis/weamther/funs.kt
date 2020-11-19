package com.smerkis.weamther

import android.util.Log
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private var formatter = SimpleDateFormat("h:mm aa", Locale.getDefault())

private var dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

fun getDateString(timeInMillis: Long): String {
    return dateFormatter.format(Date(timeInMillis * 1000))
}

fun getTimeString(timeInMillis: Long): String {
    return formatter.format(Date(timeInMillis * 1000))
}

private val df = DecimalFormat("###.#")

fun getTemperature(temp: Double): String {
    return "${df.format(temp)}°C"
}

fun Any.logD(msg: String, tag: String = this::class.simpleName.toString()) {
    Log.d(tag, msg)
}

fun Any.logI(msg: String, tag: String = this::class.simpleName.toString()) {
    Log.i(tag, msg)
}

fun Any.logE(msg: String, tag: String = this::class.simpleName.toString()) {
    Log.e(tag, msg)

}

