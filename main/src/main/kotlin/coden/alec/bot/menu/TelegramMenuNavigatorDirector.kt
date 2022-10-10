package coden.alec.bot.menu

import coden.alec.app.menu.MenuNavigatorFactory
import coden.menu.MenuView
import coden.menu.NavigationResult


class TelegramMenuNavigatorDirector(private val factory: MenuNavigatorFactory) {
    private val menus = HashMap<String, TelegramMenuNavigator>()

    fun createNewMainMenu(): MenuView {
        val navigator = TelegramMenuNavigator(factory.mainMenuNavigator())
        val menu = navigator.createMainMenu()
        captureHandleableIds(menu, navigator)
        return menu
    }

    fun handleCommand(dest: String): Result<NavigationResult> {
        return menus[dest]?.let {
            Result.success(it.navigate(dest))
        } ?: Result.failure(IllegalArgumentException("Navigation Destination with id $dest does not exist"))
    }

    private fun captureHandleableIds(menuView: MenuView, navigator: TelegramMenuNavigator) {
        menuView.items.forEach {
            menus[it.id] = navigator
        }
        menuView.backItemView?.let {
            menus[it.id] = navigator
        }
    }

}