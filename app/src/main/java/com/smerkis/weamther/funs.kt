package com.smerkis.weamther

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showInfoDialog(title: String, message: String?, call: (() -> Unit)? = null) {
    AlertDialog.Builder(this.activity as Context)
        .setTitle(title)
        .setMessage(message ?: getString(R.string.dialog_missing_message))
        .setCancelable(false)
        .setPositiveButton("Ok") { _, _ ->
            call?.invoke()
        }
        .create()
        .show()
}

fun Double.toString() {
    this.toString()
}

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