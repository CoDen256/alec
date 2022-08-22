package coden.alec.app.states

import kotlin.reflect.KClass

data class Entry(
    val input: State,
    val command: KClass<out Command>,
    val condition: (Command) -> Boolean,
    val output: State,
    val action: (Command) -> Unit,
) {
    companion object {
        fun entry(
            input: State,
            command: KClass<out Command>,
            output: State,
            action: (Command) -> Unit,
            condition: (Command) -> Boolean = { true },
        ): Entry {
            return Entry(input, command, condition, output, action)
        }

        fun entry(
            input: State,
            command: Command,
            output: State,
            action: (Command) -> Unit,
            condition: (Command) -> Boolean = { true },
        ): Entry {
            return Entry(input, command::class, condition, output, action)
        }
    }
}