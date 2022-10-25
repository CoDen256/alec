package coden.alec.bot.menu

import coden.alec.app.menu.MenuExecutor
import coden.alec.app.views.ErrorView
import coden.alec.app.views.MenuView
import coden.fsm.Command

class TelegramMenuExecutor(
    private val errorView: ErrorView,
    private val menuView: MenuView,
    private val director: TelegramMenuNavigatorDirector,
): MenuExecutor {
    override fun displayMenu(){
        menuView.displayMenu(director.createNewMainMenu())
    }

    override fun navigate(dest: String): Command?{
        return director.handleCommand(dest).map { result ->
            menuView.displayMenu(result.menu)
            result.action
        }.onFailure { throwable ->
            throwable.message?.let { msg -> errorView.displayError(msg) }
        }.getOrNull()
    }
}