package com.smerkis.weamther.activities

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.smerkis.weamther.R

abstract class AbstractActivity : AppCompatActivity() {
    @SuppressLint("LongNotTimber")
    fun logD(msg: String) {
        Log.d(this.javaClass.simpleName + "TAG", msg)
    }

    @SuppressLint("LongNotTimber")
    fun logE(msg: String) {
        Log.e(this.javaClass.simpleName + "TAG", msg)
    }

    @SuppressLint("LongNotTimber")
    fun logI(msg: String) {
        Log.i(this.javaClass.simpleName + "TAG", msg)
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun toastLong(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun showInfoDialog(title: String, message: String?, call: (() -> Unit)? = null) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message ?: getString(R.string.dialog_missing_message))
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                call?.invoke()
            }
            .create()
            .show()
    }
}