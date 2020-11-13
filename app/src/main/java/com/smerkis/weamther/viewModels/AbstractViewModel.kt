package com.smerkis.weamther.viewModels

import androidx.lifecycle.ViewModel

abstract class AbstractViewModel : ViewModel() {

    fun String.capitalize(): String {
        return this[0].toUpperCase() + this.substring(1, this.length)
    }

}