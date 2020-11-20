package com.smerkis.weamther.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepo {

    protected suspend fun <T> getFlow(call: () -> T) = flow<T> {
        call.invoke()?.let {
            emit(it)
        }
    }.catch {
    }.flowOn(Dispatchers.IO)
}

