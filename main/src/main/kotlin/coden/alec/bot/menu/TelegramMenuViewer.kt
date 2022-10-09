package coden.alec.bot.menu

import coden.alec.ui.menu.MenuItemView
import coden.alec.ui.menu.MenuNavigator
import coden.alec.ui.menu.MenuView
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton


class TelegramMenuViewer(
    private val navigator: MenuNavigator,
    private val itemsPerRow: Int = 4
) {
    fun createMain(): Pair<String, ReplyMarkup>{
        return menuViewToTelegramMarkup(navigator.createMain())
    }

    fun navigate(destination: String): Pair<String, ReplyMarkup> {
        return menuViewToTelegramMarkup(navigator.navigate(destination))
    }

    private fun menuViewToTelegramMarkup(menuView: MenuView): Pair<String, ReplyMarkup> {
        return menuView.description to menuToMarkup(menuView.itemRows, menuView.backItem)
    }

    private fun menuToMarkup(items: List<MenuItemView>, backView: MenuItemView?): InlineKeyboardMarkup {
        val result = ArrayList<List<InlineKeyboardButton>>()
        generateSequence(0) { n -> n + itemsPerRow }
            .take((items.size + (itemsPerRow - 1)) / itemsPerRow)
            .forEach { tupleIndex ->
                val row = ArrayList<InlineKeyboardButton>()
                (tupleIndex until (tupleIndex + itemsPerRow).coerceAtMost(items.size)).map {
                    row.add(menuItemToButton(items[it]))
                }
                result.add(row)
            }
        backView?.let {
            result.add(listOf(menuItemToButton(it)))
        }
        return InlineKeyboardMarkup.create(result)
    }

    private fun menuItemToButton(item: MenuItemView): InlineKeyboardButton {
        return InlineKeyboardButton.CallbackData(item.name, callbackData = item.id)
    }
}
