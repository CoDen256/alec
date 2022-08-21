package coden.alec.app.states

import kotlin.reflect.KClass

data class Entry(
    val input: State,
    val command: KClass<out Command>,
    val condition: TransitionCondition,
    val output: State,
    val action: (Command) -> Boolean,
    val fallback: State,
    val fallbackAction: (Command) -> Unit
) {
    companion object {
        fun entry(
            input: State,
            command: KClass<out Command>,
            output: State,
            action: (Command) -> Boolean,
            condition: TransitionCondition = True,
            fallback: State = input,
            fallbackAction: (Command) -> Unit = {}
        ): Entry {
            return Entry(input, command, condition, output, action, fallback, fallbackAction)
        }

        fun entry(
            input: State,
            command: Command,
            output: State,
            action: (Command) -> Boolean,
            condition: TransitionCondition = True,
            fallback: State = input,
            fallbackAction: (Command) -> Unit = {}
        ): Entry {
            return Entry(input, command::class, condition, output, action, fallback, fallbackAction)
        }
    }
}