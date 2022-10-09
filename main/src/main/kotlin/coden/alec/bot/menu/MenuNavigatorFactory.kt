package coden.alec.bot.menu

import coden.alec.ui.menu.MenuLayout
import coden.alec.ui.menu.MenuNavigator

class MenuNavigatorFactory(private val menuLayout: MenuLayout) {
    fun mainMenuNavigator(): MenuNavigator {
        return MenuNavigator(menuLayout)
    }
}