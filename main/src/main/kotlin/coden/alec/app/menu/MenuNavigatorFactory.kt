package coden.alec.app.menu

import coden.menu.MenuLayout
import coden.menu.MenuNavigator

class MenuNavigatorFactory(private val menuLayout: MenuLayout) {
    fun mainMenuNavigator(): MenuNavigator {
        return MenuNavigator(menuLayout)
    }
}