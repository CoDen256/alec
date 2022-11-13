package coden.alec.app.util

import coden.alec.app.actuators.InternalError
import coden.alec.app.actuators.UserException

fun String.sub(vararg vars: String ): String{
    var result = this
    vars.forEach {
        result = result.replaceFirst("%s", it)
    }
    return result
}

 inline fun <R, T> Result<T>.get(
    onSuccess: (T) -> R,
    onUserException: (Throwable) -> R,
    onInternalError: (Throwable) -> R,
): R {
    return fold(onSuccess) {
        when(it){
            is UserException -> onUserException(it)
            is InternalError -> onInternalError(it)
            else -> throw IllegalStateException("Invalid Exception is thrown $it")
        }
    }
}