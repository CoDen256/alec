package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.console.menu.ConsoleMenuReindexingNavigator
import coden.alec.console.view.ConsoleView
import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.console.view.ConsoleMenuFormatter
import coden.fsm.StateExecutor

class ConsoleApp(
    private val view: ConsoleView,
    private val stateExecutor: StateExecutor,
    private val menuFactory: MenuNavigatorFactory
    ) {

    fun start() {
        val menuViewer = ConsoleMenuReindexingNavigator(menuFactory.mainMenuNavigator())
        val formatter = ConsoleMenuFormatter()
        view.displayMainMenu(formatter.format(menuViewer.createMain()))
        stateExecutor.submit(HelpCommand)
        while (true) {
            val input = readLine() ?: break
            if (input.startsWith("/")){
                if (input.startsWith("/help")){
                    stateExecutor.submit(HelpCommand)
                }
                if (input.startsWith("/text")){
                    stateExecutor.submit(TextCommand(input.split("/text ")[1]))
                }
            }else {
                menuViewer.navigate(input).onSuccess { result ->
                    result.action?.let { stateExecutor.submit(it) }
                    view.displayMessage(formatter.format(result.menu))
                }.onFailure { error ->
                    error.message?.let {  view.displayError(it) }
                }
            }
        }
    }

}

