package coden.alec.app.util

import coden.alec.app.actuators.InternalException
import coden.alec.app.actuators.UserException

fun String.sub(vararg vars: String ): String{
    var result = this
    vars.forEach {
        result = result.replaceFirst("%s", it)
    }
    return result
}

 inline fun <R, T> Result<T>.unpack(
     onSuccess: (T) -> R,
     onUserException: (UserException) -> R = {throw IllegalStateException("User Exception is thrown and cannot be handled $it")},
     onInternalError: (InternalException) -> R = {throw IllegalStateException("Internal Error is thrown and cannot be handled $it")},
): R {
    return fold(onSuccess) {
        when(it){
            is UserException -> onUserException(it)
            is InternalException -> onInternalError(it)
            else -> throw IllegalStateException("Invalid Exception is thrown $it and cannot be handled")
        }
    }
}