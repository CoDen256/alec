package coden.alec.app.states

data class Entry(
    val input: State,
    val command: Command,
    val output: State,
    val action: Action
) {
    companion object {
        fun entry(
            input: State,
            command: Command,
            output: State,
            action: Action
        ): Entry {
            return Entry(input, command, output, action)
        }
    }
}