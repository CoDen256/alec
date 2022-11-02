package coden.console

import coden.console.dispatcher.CommandRequest
import coden.console.read.CommandParser

class AliasBasedCommandParser(
    private val aliasMappers: List<AliasMapper>
) : CommandParser {

    override fun parseCommand(input: String?): CommandRequest? {
        return input?.let { parse(map(it)) }
    }

    // only '/command arg0 arg1 arg2' and '/command' are accepted
    private fun parse(input: String): CommandRequest {
        if (input.isBlank()) throw IllegalArgumentException("Input should not be blank")
        val command = input.split(" ", limit = 2)
        return CommandRequest(command[0].drop(1), command.lastOrNull().orEmpty())
    }

    private fun map(input: String): String {
        return aliasMappers.firstOrNull { it.canMap(input) }?.map(input) ?: input
    }
}

interface AliasMapper {
    fun canMap(input: String): Boolean
    fun map(input: String): String
}

