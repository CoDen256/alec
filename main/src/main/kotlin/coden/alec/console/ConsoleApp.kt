package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.app.views.View
import coden.alec.console.menu.ConsoleMenuReindexingNavigator
import coden.alec.console.view.ConsoleMenuFormatter
import coden.fsm.StateExecutor

class ConsoleApp(
    private val view: View,
    private val stateExecutor: StateExecutor,
    private val menuFactory: MenuNavigatorFactory
) {

    private val menuViewer = ConsoleMenuReindexingNavigator(menuFactory.mainMenuNavigator())
    private val formatter = ConsoleMenuFormatter()

    fun start() {
        view.displayMenu(formatter.format(menuViewer.createMain()))
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
                    view.displayMenu(formatter.format(result.menu))
                }.onFailure { error ->
                    error.message?.let {  view.displayError(it) }
                }
            }
        }
    }

}

