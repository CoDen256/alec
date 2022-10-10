package coden.alec.bot.view

import coden.menu.ItemView
import coden.menu.MenuView
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class TelegramMenuFormatter(private val itemsPerRow: Int = 4) {
    fun format(menu: MenuView): TelegramMessageResponse{
        return TelegramMessageResponse(menu.description, itemsToPlainString(menu.items, menu.backItemView))
    }

    private fun itemsToPlainString(items: List<ItemView>, backView: ItemView?): InlineKeyboardMarkup {
        val result = ArrayList<List<InlineKeyboardButton>>()
        generateSequence(0) { n -> n + itemsPerRow }
            .take((items.size + (itemsPerRow - 1)) / itemsPerRow)
            .forEach { tupleIndex ->
                result.add(createRow(tupleIndex, items))
            }
        backView?.let {
            result.add(listOf(menuItemToInlineButton(it)))
        }
        return InlineKeyboardMarkup.create(result)
    }

    private fun createRow(tupleIndex: Int, items: List<ItemView>): List<InlineKeyboardButton> {
        return (tupleIndex until (tupleIndex + itemsPerRow).coerceAtMost(items.size)).map {
            menuItemToInlineButton(items[it])
        }
    }

    private fun menuItemToInlineButton(item: ItemView): InlineKeyboardButton {
        return InlineKeyboardButton.CallbackData(item.name, callbackData = item.id)
    }
}

class TelegramMessageResponse(
    val message: String,
    val replyMarkup: ReplyMarkup
)