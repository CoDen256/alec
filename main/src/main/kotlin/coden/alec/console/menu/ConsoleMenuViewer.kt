package coden.alec.console.menu

import coden.alec.ui.menu.MenuItemView
import coden.alec.ui.menu.MenuNavigator
import coden.alec.ui.menu.MenuView
import coden.fsm.Command

class ConsoleMenuViewer(
    private val navigator: MenuNavigator,
) {

    fun createMain(): String{
        return menuViewToTelegramMarkup(navigator.createMain())
    }

    fun navigate(destination: String): Pair<String, Command?> {
        val menuView = navigator.navigate(destination)
        return menuViewToTelegramMarkup(menuView) to menuView.action
    }

    private fun menuViewToTelegramMarkup(menuView: MenuView): String {
        return menuView.description +"\n"+ menuToStringMenu(menuView.itemRows, menuView.backItem)
    }

    private fun menuToStringMenu(items: List<MenuItemView>, backView: MenuItemView?): String {
        val result = StringBuilder()
        items.forEach {
            result.appendLine(menuItemToSelection(it))
        }
        backView?.let {
            result.appendLine(menuItemToSelection(it))
        }
        return result.toString()
    }

    private fun menuItemToSelection(item: MenuItemView): String {
        return "[${item.name}]"
    }
}