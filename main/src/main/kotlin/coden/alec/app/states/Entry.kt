package coden.alec.app.states

data class Entry(
    val input: State,
    val condition: TransitionCondition,
    val output: State,
    val action: Action
) {
    companion object {
        fun entry(
            input: State,
            output: State,
            condition: TransitionCondition,
            action: Action
        ): Entry {
            return Entry(input, condition, output, action)
        }

        fun entry(
            input: State,
            output: State,
            command: Command,
            action: Action
        ): Entry {
            return entry(input, output, CommandEquals(command), action)
        }
    }
}