package coden.alec.bot

import coden.alec.app.messages.MessageResource
import coden.alec.main.Menu
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlin.collections.ArrayList



class MenuNavigatorFactory(
    private val mainMenuTemplate: Menu,
    private val executor: StateExecutor,
    private val messages: MessageResource,
) {
    fun mainMenuNavigator(): MenuNavigator{
        return MenuNavigator(mainMenuTemplate, executor, messages)
    }
}

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
        return menuView.description to menuToMarkup(menuView.itemRows, menuView.backView)
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

class MenuView(
    val description: String,
    val itemRows: List<MenuItemView>,
    val backView: MenuItemView?
)

class MenuItemView(
    val name: String,
    val id: String
)

class MenuNavigator (
    private val mainMenuTemplate: Menu,
    private val executor: StateExecutor,
    messages: MessageResource,
){

    private val backCommand = "MenuNavigator.BACK"
    private val backView = MenuItemView(messages.menuBackMessage, id = backCommand)
    private val parentStack = ArrayList<Menu>()
    private var current: Menu = mainMenuTemplate

    fun createMain(): MenuView {
        return MenuView(
            mainMenuTemplate.description,
            menuItemsToView(mainMenuTemplate.items),
            parentStack.lastOrNull()?.let { backView })
    }


    fun navigate(data: String): MenuView {
        current = moveToNext(data) ?: current
        return MenuView(
            current.description,
            menuItemsToView(current.items),
            parentStack.lastOrNull()?.let { backView })
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


    private fun menuItemsToView(items: List<Menu>): List<MenuItemView> {
        return items.map { menuItemToView(it) }
    }

    private fun menuItemToView(item: Menu): MenuItemView {
        return MenuItemView(item.name, id = item.name)
    }
}