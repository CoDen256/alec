package coden.fsm

import kotlin.reflect.KClass

interface State

interface Command {
    val arguments: Result<String>
}

abstract class BaseCommand(arguments: String? = null): Command {
    override val arguments: Result<String> = arguments?.let {
        Result.success(it)
    } ?: Result.failure(NoArgException())

    override fun toString(): String {
        return this.javaClass.simpleName + arguments.map {"($it)"}.getOrDefault("")
    }
}

class NoArgException: RuntimeException("No command argument")

data class FSM(val start: State, val table: FSMTable)

open class FSMTable(private val entries: List<Entry>): ArrayList<Entry>(entries) {
    constructor(vararg entries: Entry) : this(arrayListOf(*entries))

    operator fun plus (table: FSMTable): FSMTable{
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