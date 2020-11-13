package com.smerkis.weamther.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepo {

    protected suspend fun <T> getFlow(call: () -> T?) = flow {
        val t = call.invoke()
        if (t != null) {
            emit(t)
        }
    }.flowOn(Dispatchers.IO).catch {  }
}