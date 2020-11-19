package com.smerkis.weamther

class NoResponseException(message: String? = MyErrorHandler.UNKNOWN_ERROR) : Exception(message)

class NoDataException(message: String? = MyErrorHandler.NO_SUCH_DATA) : Exception()