package coden.alec.app.states

import java.lang.IllegalArgumentException

interface Command {
    val arguments: Result<String>
}

abstract class BaseCommand(arguments: String? = null): Command{
    override val arguments: Result<String> = arguments?.let {
        Result.success(it)
    } ?: Result.failure(IllegalArgumentException("No argument provided"))
}

object HelpCommand : BaseCommand()
object ListScalesCommand : BaseCommand()
object CreateScaleCommandNoArgs: BaseCommand()
class CreateScaleCommand(arguments: String) : BaseCommand(arguments)
class TextCommand(text: String): BaseCommand(text)
