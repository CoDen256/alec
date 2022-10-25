package coden.alec.console.menu

import coden.alec.app.menu.MenuExecutor
import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.app.views.ErrorView
import coden.alec.app.views.MenuView
import coden.fsm.Command

class ConsoleMenuExecutor
    (
    private val errorView: ErrorView,
    private val menuView: MenuView,
    private val menuViewer: ConsoleMenuReindexingNavigator
            )
    : MenuExecutor {

    override fun displayMenu() {
        menuView.displayMenu(menuViewer.createMain())
    }

    override fun navigate(dest: String): Command? {
        return menuViewer.navigate(dest).map { result ->
            menuView.displayMenu(result.menu)
            result.action
        }.onFailure { throwable ->
            throwable.message?.let { msg -> errorView.displayError(msg) }
        }.getOrNull()
    }
}