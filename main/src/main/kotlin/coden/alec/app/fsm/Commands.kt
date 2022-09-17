package coden.alec.app.fsm

import coden.fsm.BaseCommand


object HelpCommand : BaseCommand()
object ListScalesCommand : BaseCommand()
object CreateScaleCommandNoArgs: BaseCommand()

class CreateScaleCommand(arguments: String) : BaseCommand(arguments)
class TextCommand(text: String): BaseCommand(text)
class InlineCommand(callbackData: String) : BaseCommand(callbackData)
