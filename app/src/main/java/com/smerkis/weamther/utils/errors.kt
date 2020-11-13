package com.smerkis.weamther.utils

import com.smerkis.weamther.repository.ErrorHandler

class NoResponseException(message: String? = ErrorHandler.UNKNOWN_ERROR) : Exception(message)
class NoDataException(message: String? = ErrorHandler.NO_SUCH_DATA) : Exception(message)