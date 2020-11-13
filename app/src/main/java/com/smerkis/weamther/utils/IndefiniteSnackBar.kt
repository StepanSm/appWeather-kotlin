package com.smerkis.weamther.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.smerkis.weamther.R

object IndefiniteSnackBar {
    private var snackbar: Snackbar? = null

    fun show(view: View, text: String, action: () -> Unit) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(view.context.getString(R.string.retry)) { action }
            show()
        }
    }

    fun hide(){
        snackbar?.dismiss()
    }
}