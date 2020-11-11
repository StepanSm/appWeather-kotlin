package com.smerkis.weamther

import android.util.Log

internal fun Class<*>.logd(message: String) {
    Log.d(this.simpleName, message)
}