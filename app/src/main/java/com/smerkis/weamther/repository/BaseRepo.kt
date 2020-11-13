package com.smerkis.weamther.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepo {

    protected suspend fun <T> getFlow(call: () -> T) = flow<Result<T>> {
        call.invoke()?.let {
            emit(Result.Success(it))
        }
    } .catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}