package coden.alec.app.states

sealed interface Command {
}

object HelpCommand : Command
object ListScalesCommand : Command
object CreateScaleCommand : Command
object CreateScaleNoArgumentCommand : Command
object TextCommand: Command
