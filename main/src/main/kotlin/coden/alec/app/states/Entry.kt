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
            action: (Command) -> Unit,
        ): Entry {
            return Entry(input, command, transition, action)
        }

        fun entry(
            input: State,
            command: Command,
            transition: (Command) -> State,
            action: (Command) -> Unit,
        ): Entry {
            return Entry(input, command::class, transition, action)
        }
    }
}