package coden.alec.bot.menu

import coden.menu.MenuLayout
import coden.menu.MenuNavigator

class MenuNavigatorFactory(private val menuLayout: MenuLayout) {
    fun mainMenuNavigator(): MenuNavigator {
        return MenuNavigator(menuLayout)
    }
}