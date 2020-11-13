package com.smerkis.weamther.repository

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Throwable,   val message: String = exception.message ?: ErrorHandler.UNKNOWN_ERROR) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception"
        }
    }
}