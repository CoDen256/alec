package coden.console.read

import coden.console.dispatcher.CommandRequest

interface CommandParser {
    fun parseCommand(input: String?): CommandRequest?
}