package com.smerkis.weamther.repository

import kotlinx.coroutines.flow.flow

abstract class BaseRepo {

    protected suspend fun <T> getFlow(call: () -> T?) = flow {
        val t = call.invoke()
        if (t != null) {
            emit(t)
        }
    }
}