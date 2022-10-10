package coden.alec.bot.menu

import coden.alec.app.menu.MenuNavigatorFactory
import coden.menu.MenuView
import coden.menu.NavigationResult


class TelegramMenuNavigatorDirector(private val factory: MenuNavigatorFactory) {
    private val navigators = ArrayList<TelegramMenuNavigator>()

    fun createNewMainMenu(): MenuView {
        val navigator = TelegramMenuNavigator(factory.mainMenuNavigator())
        val menu = navigator.createMainMenu()
        navigators.add(navigator)
        return menu
    }

    fun handleCommand(dest: String): Result<NavigationResult> {
        return findNavigatorByDestination(dest)?.let {
            Result.success(it.navigate(dest))
        } ?: Result.failure(IllegalArgumentException("Navigation Destination with id $dest does not exist"))
    }

    private fun findNavigatorByDestination(dest: String): TelegramMenuNavigator? {
        return navigators.find { it.canNavigate(dest) }
    }
}