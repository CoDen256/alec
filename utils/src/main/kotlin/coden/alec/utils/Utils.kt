package coden.alec.utils


 inline fun <R, T> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return fold(
        {transform(it)},
        {Result.failure(it)}
    )
}

inline fun <R, T> Result<T>.combine(other: (T) -> Result<R>): Result<Pair<T, R>> {
    return flatMap { first -> other(first).map { second -> first to second }  }
}

inline fun <T> Result<T>.then(apply: (T) -> Unit): Result<T>{
    return map { apply(it); it }
}