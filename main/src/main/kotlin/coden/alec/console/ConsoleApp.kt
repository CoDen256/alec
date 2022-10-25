package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.app.menu.MenuPresenter
import coden.fsm.StateExecutor

class ConsoleApp(
    private val stateExecutor: StateExecutor,
    private val menuExecutor: MenuPresenter
) {

    fun start() {
        menuExecutor.displayMenu()
        stateExecutor.submit(HelpCommand)
        while (true) {
            val input = readlnOrNull() ?: break
            if (input.startsWith("/")){
                if (input.startsWith("/help")){
                    stateExecutor.submit(HelpCommand)
                }
                if (input.startsWith("/text")){
                    stateExecutor.submit(TextCommand(input.split("/text ")[1]))
                }
            }else {
                menuExecutor.navigate(input)?.let {
                    stateExecutor.submit(it)
                }
            }
        }
    }

}

