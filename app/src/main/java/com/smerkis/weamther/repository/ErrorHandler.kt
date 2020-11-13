package com.smerkis.weamther.repository

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory
import com.smerkis.weamther.utils.IndefiniteSnackBar
import com.smerkis.weamther.utils.NoDataException
import com.smerkis.weamther.utils.NoResponseException
import okhttp3.ResponseBody
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.lang.Exception


object ErrorHandler {

    private const val NETWORK_ERROR_MESSAGE =
        "Please check your internet connectivity and try again!"
    private const val EMPTY_RESPONSE = "Server returned empty response."
    const val NO_SUCH_DATA = "Data not found in the database"
    const val UNKNOWN_ERROR = "An unknown error occurred!"

//    fun handleError(
//        view: View,
//        throwable: Error,
//        shouldShowToast: Boolean = false,
//        shouldShowSnackBar: Boolean = false,
//        refreshAction: () -> Unit = {}
//    ) {
//        if (shouldShowSnackBar) {
//            showSnackBar(view, message = throwable.message, refresh = refreshAction)
//        } else {
//            if (shouldShowToast) {
//                showLongToast(view.context, throwable.message)
//            }
//        }
//
//        when (throwable.exception) {
//            is IOException -> Timber.e(NETWORK_ERROR_MESSAGE)
//            is HttpException -> Timber.e("HTTP Exception ${throwable.exception.code()}")
//            is NoResponseException -> Timber.e(NO_SUCH_DATA)
//            is NoDataException -> Timber.e(NO_SUCH_DATA)
//            else -> Timber.e(throwable.message)
//        }
//    }

    private fun showLongToast(context: Context, message: String) = Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()

    private fun showSnackBar(view: View, message: String, refresh: () -> Unit = {}) {
        IndefiniteSnackBar.show(view, message, refresh)

    }

}