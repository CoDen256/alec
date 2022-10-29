package coden.fsm

interface CommandExecutor {
    fun submit(command: Command)
}