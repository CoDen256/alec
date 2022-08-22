package coden.alec.app.states

import kotlin.reflect.KClass

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