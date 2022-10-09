package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.bot.MenuItemView
import coden.alec.bot.MenuNavigator
import coden.alec.bot.MenuNavigatorFactory
import coden.alec.bot.MenuView
import coden.alec.console.view.ConsoleView
import coden.fsm.StateExecutor

class ConsoleApp(
    private val view: ConsoleView,
    private val stateExecutor: StateExecutor,
    private val menuFactory: MenuNavigatorFactory
    ) {

    fun start() {
        val menuViewer = ConsoleMenuViewer(menuFactory.mainMenuNavigator())
        println(menuViewer.createMain())
        stateExecutor.submit(HelpCommand)
        while (true) {
            val input = readLine() ?: break
            println(menuViewer.navigate(input))
        }
    }

}

class ConsoleMenuViewer(
    private val navigator: MenuNavigator,
) {

    fun createMain(): String{
        return menuViewToTelegramMarkup(navigator.createMain())
    }

    fun navigate(destination: String): String {
        return menuViewToTelegramMarkup(navigator.navigate(destination))
    }

    private fun menuViewToTelegramMarkup(menuView: MenuView): String {
        return menuView.description +"\n"+ menuToStringMenu(menuView.itemRows, menuView.backView)
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