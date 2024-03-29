package coden.display.menu

import coden.display.displays.ErrorDisplay
import coden.display.displays.MenuDisplay
import coden.fsm.Command

class BaseMenuPresenter(
    private val errorDisplay: ErrorDisplay,
    private val menuDisplay: MenuDisplay,
    private val navigator: MenuNavigator
) : MenuPresenter {

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