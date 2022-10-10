package coden.alec.bot.menu

import coden.menu.MenuNavigator
import coden.menu.MenuView
import coden.menu.NavigationResult

class TelegramMenuNavigator(private val navigator: MenuNavigator) {

    fun canNavigate(destination: String): Boolean {
        return navigator.canNavigate(destination)
    }

    fun createMainMenu(): MenuView{
        return navigator.createMainMenu()
    }

    fun navigate(destination: String): NavigationResult {
        return navigator.navigate(destination)
    }
}
