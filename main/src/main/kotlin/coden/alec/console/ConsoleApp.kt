package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.display.menu.MenuPresenter
import coden.fsm.CommandExecutor

class ConsoleApp(
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter
) {

    fun start() {
        menuExecutor.displayMenu()
        commandExecutor.submit(HelpCommand)
        while (true) {
            val input = readlnOrNull() ?: break
            if (input.startsWith("/")){
                if (input.startsWith("/help")){
                    commandExecutor.submit(HelpCommand)
                }
                if (input.startsWith("/text")){
                    commandExecutor.submit(TextCommand(input.split("/text ")[1]))
                }
            }else {
                menuExecutor.navigate(input)?.let {
                    commandExecutor.submit(it)
                }
            }
        }
    }

}

