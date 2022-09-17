package coden.alec.bot

import coden.alec.app.messages.MessageResource
import coden.alec.main.Menu
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlin.collections.ArrayList

class MenuControllerFactory(
    private val menu: Menu,
    private val executor: StateExecutor,
    private val messages: MessageResource,
    private val itemsPerRow: Int = 4
) {
    fun controller(): MenuController{
        return MenuController(menu, executor, messages, itemsPerRow)
    }
}

class MenuController (
    private val menu: Menu,
    private val executor: StateExecutor,
    private val messages: MessageResource,
    private val itemsPerRow: Int = 4
){

    private val backCommand = "MenuController.BACK"
    private val parentStack = ArrayList<Menu>()
    private var current: Menu = menu

    fun create(): Pair<String, ReplyMarkup> {
        return menu.description to menuToMarkup(menu.items, parentStack.lastOrNull())
    }


    fun submit(data: String): Pair<String, ReplyMarkup>{
        val target = if (data == backCommand) {parentStack.removeLastOrNull() }
        else current.items.find { it.name == data }?.also { parentStack.add(current) }

        target?.also {
            current = it
        }
        return (current.description to menuToMarkup(current.items, parentStack.lastOrNull()))
    }


    private fun menuToMarkup(items: List<Menu>, parent: Menu?): InlineKeyboardMarkup {
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
        parent?.let {
            result.add(listOf(InlineKeyboardButton.CallbackData(messages.backButtonMessage, callbackData = backCommand)))
        }
        return InlineKeyboardMarkup.create(result)
    }

    private fun menuItemToButton(item: Menu): InlineKeyboardButton {
        return InlineKeyboardButton.CallbackData(item.name, callbackData = item.name)
    }
}