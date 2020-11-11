package com.smerkis.weamther.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepo {

    protected fun <T> getFlow(call: () -> T?): Flow<T> {
        return flow {
            val t = call.invoke()
            if (t != null) {
                emit(t)
            }
        }.flowOn(Dispatchers.IO)
    }
}