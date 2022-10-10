package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.app.views.View
import coden.alec.console.menu.ConsoleMenuReindexingNavigator
import coden.fsm.StateExecutor

class ConsoleApp(
    private val view: View,
    private val stateExecutor: StateExecutor,
    private val menuFactory: MenuNavigatorFactory
) {
    private val menuViewer = ConsoleMenuReindexingNavigator(menuFactory.mainMenuNavigator())

    fun start() {
        view.displayMenu(menuViewer.createMain())
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
                    view.displayMenu(result.menu)
                }.onFailure { error ->
                    error.message?.let {  view.displayError(it) }
                }
            }
        }
    }

}

