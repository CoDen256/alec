package coden.display.menu

import coden.menu.MenuView
import coden.menu.NavigationResult

interface MenuNavigator {
    fun createMainMenu(): MenuView
    fun navigate(destination: String): Result<NavigationResult>
}