package coden.alec.app.states

sealed interface Command {
}

object Help : Command
object ListScales : Command
object CreateScale : Command
object CreateScaleNoArgs : Command
object Text: Command
