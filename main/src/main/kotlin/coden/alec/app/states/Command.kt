package coden.alec.app.states


interface Command {
    val arguments: Result<String>
}

abstract class BaseCommand(arguments: String? = null): Command{
    override val arguments: Result<String> = arguments?.let {
        Result.success(it)
    } ?: Result.failure(NoArgException())
}

object HelpCommand : BaseCommand()
object ListScalesCommand : BaseCommand()
object CreateScaleCommandNoArgs: BaseCommand()

object HelpInlineCommand : BaseCommand()
object ListScalesInlineCommand : BaseCommand()
object CreateScaleInlineCommand: BaseCommand()

class CreateScaleCommand(arguments: String) : BaseCommand(arguments)
class TextCommand(text: String): BaseCommand(text)

class NoArgException: RuntimeException("No argument")