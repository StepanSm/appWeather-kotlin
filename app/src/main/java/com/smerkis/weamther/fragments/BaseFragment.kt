package com.smerkis.weamther.fragments

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.smerkis.weamther.R

abstract class BaseFragment(layoutId: Int) :
    Fragment(layoutId) {


    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDi()
    }


    abstract fun initDi()


    fun showInfoDialog(title: String, message: String?, call: (() -> Unit)? = null) {
        AlertDialog.Builder(activity as Context)
            .setTitle(title)
            .setMessage(message ?: getString(R.string.dialog_missing_message))
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                call?.invoke()
            }
            .create()
            .show()
    }

    fun showShortToast(msg: String) {
        Toast.makeText(activity as Context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(msg: String) {
        Toast.makeText(activity as Context, msg, Toast.LENGTH_LONG).show()
    }
}