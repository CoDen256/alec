package coden.bot.menu

import coden.display.menu.MenuNavigator
import coden.menu.LayoutBasedMenuNavigatorFactory
import coden.menu.LayoutBasedMenuNavigator
import coden.menu.MenuView
import coden.menu.NavigationResult


class TelegramAggregatedMenuNavigator(private val factory: LayoutBasedMenuNavigatorFactory): MenuNavigator {
    private val navigators = ArrayList<LayoutBasedMenuNavigator>()

    override fun createMainMenu(): MenuView {
        val navigator = factory.newMenuNavigator()
        val menu = navigator.createMainMenu()
        navigators.add(navigator)
        return menu
    }

    override fun navigate(destination: String): Result<NavigationResult> {
        return findNavigatorByDestination(destination)?.let {
            Result.success(it.navigate(destination))
        } ?: Result.failure(IllegalArgumentException("Navigation Destination with id $destination does not exist"))
    }

    private fun findNavigatorByDestination(dest: String): LayoutBasedMenuNavigator? {
        return navigators.find { it.canNavigate(dest) }
    }
}