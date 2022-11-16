package coden.alec.app.fsm

import coden.fsm.BaseCommand

object HelpCommand : BaseCommand()

object ListScalesCommand : BaseCommand()
object CreateScaleCommandNoArgs: BaseCommand()
object DeleteScaleCommandNoArgs: BaseCommand()

class CreateScaleCommand(arguments: String) : BaseCommand(arguments)
class DeleteScaleCommand(arguments: String): BaseCommand(arguments)

class TextCommand(text: String): BaseCommand(text)

