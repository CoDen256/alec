package coden.alec.console.view

import coden.menu.ItemView
import coden.menu.MenuView

class ConsoleMenuFormatter {
    fun format(menu: MenuView): String{
        return menu.description + "\n" + itemsToPlainString(menu.items, menu.backItemView)
    }

    private fun itemsToPlainString(items: List<ItemView>, backView: ItemView?): String {
        val result = StringBuilder()
        items.forEach {
            result.appendLine(menuItemToSelection(it))
        }
        backView?.let {
            result.appendLine(menuItemToSelection(it))
        }
        return result.toString()
    }

    private fun menuItemToSelection(item: ItemView): String {
        return "[${item.id}] ${item.name}"
    }
}