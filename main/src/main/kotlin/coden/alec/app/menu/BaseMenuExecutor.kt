package coden.alec.app.menu

import coden.alec.app.views.ErrorDisplay
import coden.alec.app.views.MenuDisplay
import coden.fsm.Command

class BaseMenuExecutor(
    private val errorDisplay: ErrorDisplay,
    private val menuDisplay: MenuDisplay,
    private val navigator: MenuNavigator
) : MenuExecutor {

    override fun displayMenu() {
        menuDisplay.displayMenu(navigator.createMainMenu())
    }

    override fun navigate(dest: String): Command? {
        return navigator.navigate(dest).map { result ->
            menuDisplay.displayMenu(result.menu)
            result.action
        }.onFailure { throwable ->
            throwable.message?.let { msg -> errorDisplay.displayError(msg) }
        }.getOrNull()
    }
}