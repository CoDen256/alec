package coden.fsm

import kotlin.reflect.KClass

interface State

interface Command {
    val arguments: Result<String>
}

abstract class BaseCommand(arguments: String? = null) : Command {
    override val arguments: Result<String> = arguments?.let {
        Result.success(it)
    } ?: Result.failure(NoArgException("No command argument for ${this.javaClass.simpleName}"))

    override fun toString(): String {
        return this.javaClass.simpleName + arguments.map { "($it)" }.getOrDefault("")
    }
}

class NoArgException(msg: String) : RuntimeException(msg) {
    constructor() : this("No command argument")
}

data class FSM(val start: State, val table: FSMTable)

open class FSMTable(private val entries: List<Entry>) : ArrayList<Entry>(entries) {
    constructor(vararg entries: Entry) : this(arrayListOf(*entries))

    operator fun plus(table: FSMTable): FSMTable {
        return FSMTable(this.entries + table.entries)
    }
}


data class Entry(
    val input: State,
    val command: KClass<out Command>,
    val transition: (Command) -> State,
) {
    companion object {
        fun entry(
            input: State,
            command: KClass<out Command>,
            transition: (Command) -> State,
        ): Entry {
            return Entry(input, command, transition)
        }

        fun entry(
            input: State,
            command: Command,
            transition: (Command) -> State,
        ): Entry {
            return Entry(input, command::class, transition)
        }

        fun entry(
            input: State,
            command: Command,
            output: State,
            action: (Command) -> Unit,
        ): Entry = entry(input, command) { action(it); output }

        fun entry(
            input: State,
            command: KClass<out Command>,
            output: State,
            action: (Command) -> Unit,
        ): Entry = entry(input, command) { action(it); output }

    }
}

fun requireArgument(action: (String) -> State): (Command) -> State{
    return {
        action(it.arguments.getOrThrow())
    }
}

fun <T> Result<T>.state(state: State): Result<State>{
    return map { state }
}

fun <T> Result<T>.state(state: (T) -> State): Result<State>{
    return map { state(it) }
}

// .on<Throwable> {do something}.on<....>{...}.on<>{}.get()
inline fun <reified E: Throwable> Result<State>.onError(crossinline handler: (E) -> State): ErrorHandlingResult<State>{
    return ErrorHandlingResult(this).onError(handler)
}

fun Result<State>.onErrors(
    vararg handlers: ExceptionHandler<Throwable, State>
): State {
    return mapToState({it}, *handlers)
}

fun <T> Result<T>.mapToState(
    toState: (T) -> State,
    vararg handlers: ExceptionHandler<Throwable, State>
): State {
    val delegate = ErrorHandlingResult(this.map(toState))
    delegate.handlers.addAll(handlers)
    return delegate.get()
}

interface ExceptionHandler<T: Throwable, R>{
    fun canHandle(throwable: T): Boolean
    fun handle(throwable: T): R
}


class ErrorHandlingResult<T>(private val result: Result<T>) {
    val handlers: MutableList<ExceptionHandler<Throwable, T>> = ArrayList()

    inline fun <reified E: Throwable> onError(crossinline handler: (E) -> T): ErrorHandlingResult<T> {
        handlers.add(handle(handler))
        return this
    }

    fun get(): T {
        return result.fold({ it }) { throwable ->
            handlers
                .firstOrNull { it.canHandle(throwable) }
                ?.handle(throwable)
                ?: throw IllegalStateException("No exception handler for ${throwable.javaClass.simpleName}")
        }
    }
}

inline fun <reified T: Throwable, R> handle(crossinline handler: (T) -> R): ExceptionHandler<Throwable, R>{
    return object: ExceptionHandler<Throwable, R>{
        override fun canHandle(throwable: Throwable): Boolean {
            return throwable is T
        }

        override fun handle(throwable: Throwable): R {
            return handler(throwable as T)
        }

    }
}

inline fun <reified T: Throwable> fallbackOn(crossinline handler: (T) -> State): ExceptionHandler<Throwable, State>{
    return handle(handler)
}