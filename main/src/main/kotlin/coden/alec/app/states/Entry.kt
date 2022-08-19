package coden.alec.app.states

data class Entry(
    val input: State,
    val output: State,
    val command: Command,
    val action: Action
) {
    companion object {
        fun entry(
            input: State,
            output: State,
            command: Command,
            action: Action
        ): Entry {
            return Entry(input, output, command, action)
        }
    }
}