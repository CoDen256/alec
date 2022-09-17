package coden.alec.bot

import coden.alec.app.messages.MessageResource
import coden.alec.main.Menu
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlin.collections.ArrayList

class MenuControllerFactory(
    private val mainMenuTemplate: Menu,
    private val executor: StateExecutor,
    private val messages: MessageResource,
    private val itemsPerRow: Int = 4
) {
    fun controller(): MenuController{
        return MenuController(mainMenuTemplate, executor, messages, itemsPerRow)
    }
}

class MenuController (
    private val mainMenuTemplate: Menu,
    private val executor: StateExecutor,
    private val messages: MessageResource,
    private val itemsPerRow: Int = 4
){

    private val backCommand = "MenuController.BACK"
    private val parentStack = ArrayList<Menu>()
    private var current: Menu = mainMenuTemplate

    fun createMain(): Pair<String, ReplyMarkup> {
        return mainMenuTemplate.description to menuToMarkup(mainMenuTemplate.items, parentStack.lastOrNull())
    }


    fun submit(data: String): Pair<String, ReplyMarkup>{
        current = moveToNext(data) ?: current
        return (current.description to menuToMarkup(current.items, parentStack.lastOrNull()))
    }

    private fun moveToNext(data: String): Menu? {
        return if (data == backCommand) {
            moveBack()
        } else {
            moveToSubMenu(data)
        }
    }

    private fun moveBack(): Menu? {
        return parentStack.removeLastOrNull()
    }

    private fun moveToSubMenu(data: String): Menu? {
        return current.items.find { it.name == data }?.let {
            it.action?.let { action -> executor.submit(action) }
            if (it.items.isEmpty()) {
                return@let null
            }
            parentStack.add(current)
            return@let it
        }
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