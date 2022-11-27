package coden.alec.utils


 inline fun <R, T> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return fold(
        {transform(it)},
        {Result.failure(it)}
    )
}

inline fun <T> Result<T>.then(apply: (T) -> Unit): Result<T>{
    return map { apply(it); it }
}