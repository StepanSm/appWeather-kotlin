package com.smerkis.weamther

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
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

fun getDateTime(timeInMillis: Long): String {
    return "${getDateString(timeInMillis)} ${getTimeString(timeInMillis)}"
}

private val df = DecimalFormat("###.#")

fun getTemperature(temp: Double): String {
    return "${df.format(temp)}°C"
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

