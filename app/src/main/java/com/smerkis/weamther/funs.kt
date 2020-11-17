package com.smerkis.weamther

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment

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