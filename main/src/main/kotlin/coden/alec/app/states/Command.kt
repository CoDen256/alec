package coden.alec.app.states

sealed interface Command {
}

object HelpCommand : Command
object ListScalesCommand : Command