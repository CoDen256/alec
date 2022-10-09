package coden.alec.console.menu

import coden.fsm.Command
import coden.menu.ItemView
import coden.menu.MenuNavigator
import coden.menu.MenuView
import java.lang.IllegalArgumentException

class ConsoleMenuController(private val navigator: MenuNavigator) {

    private val destinationMappingContext = HashMap<String, String>()

    fun createMain(): String {
        destinationMappingContext.clear()
        return menuToPlainString(navigator.createMainMenu())
    }

    fun navigate(destination: String): Result<ConsoleView> {
        val innerDestination = destinationMappingContext[destination]
            ?: return Result.failure(IllegalArgumentException("Selection $destination is invalid"))
        val result = navigator.navigate(innerDestination)
        destinationMappingContext.clear()
        return Result.success(
            ConsoleView(
                menuToPlainString(result.menu),
                result.action
            )
        )
    }

    private fun menuToPlainString(menu: MenuView): String {
        return menu.description + "\n" + itemsToPlainString(menu.items, menu.backItemView)
    }

    private fun itemsToPlainString(items: List<ItemView>, backView: ItemView?): String {
        val result = StringBuilder()
        var option = 0
        items.forEach {
            result.appendLine(menuItemToSelection(it, option))
            option += 1
        }
        backView?.let {
            result.appendLine(menuItemToSelection(it, option))
        }
        return result.toString()
    }

    private fun menuItemToSelection(item: ItemView, option: Int): String {
        destinationMappingContext[option.toString()] = item.id
        return "[$option] ${item.name}"
    }
}

data class ConsoleView(
    val message: String,
    val action: Command?
)

data class ConsoleMenuView(
    val description: String,
    val items: List<ConsoleMenuItemView>
)

data class ConsoleMenuItemView(
    val id: String,
    val ite
)