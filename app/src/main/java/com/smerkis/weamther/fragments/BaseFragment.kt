package com.smerkis.weamther.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.MainActivity
import org.koin.core.component.KoinApiExtension
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseFragment(layoutId: Int) :
    Fragment(layoutId) {


    fun showInfoDialog(
        title: String,
        view: View? = null,
        message: String?,
        call: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(activity as Context)
            .setTitle(title)
            .setMessage(message ?: getString(R.string.dialog_missing_message))
            .setCancelable(false)
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }
            .setView(view)
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



    @KoinApiExtension
    protected fun createToolbar(
        toolbar: Toolbar,
        shouldSetUpBtn: Boolean = true,
        title: String = ""
    ) {
        toolbar.title = title
        setHasOptionsMenu(true)
        (activity as MainActivity).setSupportActionBar(toolbar)
        if (shouldSetUpBtn) {
            activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

            toolbar.apply {
                setNavigationIcon(R.drawable.ic_up_home)
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }


    protected open fun handleErrorCode(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException,
            is ConnectException -> showShortToast(getString(R.string.network_connect))
            is HttpException -> {
                when (throwable.code()) {
                    404 -> showShortToast("information not found")
                }
            }
            else -> showShortToast(throwable.message.toString())
        }
    }

}