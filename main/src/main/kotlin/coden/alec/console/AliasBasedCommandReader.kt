package coden.alec.console

import coden.console.dispatcher.CommandRequest
import coden.console.read.CommandReader
import java.util.regex.Pattern

class AliasBasedCommandReader(
    private val aliasMappers: List<AliasMapper>
    ) : CommandReader {
    override fun read(): CommandRequest? {
        return readlnOrNull()?.let {input ->
            parse( map(input))
        }
    }

    private fun parse(input: String): CommandRequest{
        val command = input.split(" ", limit = 2)
        return CommandRequest(command[0], command[1])
    }

    private fun map(input: String): String{
        aliasMappers.firstOrNull { it.canMap(input) }?.map(input) ?:
    }
}

interface AliasMapper{
    fun canMap(input: String): Boolean
    fun map(input: String): String
}

class RegexBasedMapper(pattern: String, private val result: String) : AliasMapper {
    private val matcher = Pattern.compile(pattern).asMatchPredicate()

    override fun canMap(input: String): Boolean {
       return matcher.test(input)
    }

    override fun map(input: String): String {
        return result
    }
}