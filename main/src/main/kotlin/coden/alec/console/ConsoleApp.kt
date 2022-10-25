package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.app.views.ErrorView
import coden.alec.app.views.MenuView
import coden.alec.console.menu.ConsoleMenuReindexingNavigator
import coden.fsm.StateExecutor

class ConsoleApp(
    private val errorView: ErrorView,
    private val menuView: MenuView,
    private val stateExecutor: StateExecutor,
    private val menuFactory: MenuNavigatorFactory
) {
    private val menuViewer = ConsoleMenuReindexingNavigator(menuFactory.mainMenuNavigator())

    fun start() {
        menuView.displayMenu(menuViewer.createMain())
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
                menuViewer.navigate(input).onSuccess { result ->
                    result.action?.let { stateExecutor.submit(it) }
                    menuView.displayMenu(result.menu)
                }.onFailure { error ->
                    error.message?.let {  errorView.displayError(it) }
                }
            }
        }
    }

}

