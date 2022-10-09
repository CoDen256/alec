package coden.alec.ui.menu

import coden.alec.app.messages.MessageResource
import coden.fsm.StateExecutor




class MenuNavigator (
    private val menuLayout: MenuLayout,
    private val executor: StateExecutor,
    backLayout: BackLayout
){

    private val backCommand = "MenuNavigator.BACK"
    private val backView = MenuItemView(backLayout.description, id = backCommand)
    private val parentStack = ArrayList<MenuLayout>()
    private var current: MenuLayout = menuLayout

    fun createMain(): MenuView {
        return MenuView(
            menuLayout.description,
            menuItemsToView(menuLayout.items),
            parentStack.lastOrNull()?.let { backView })
    }


    fun navigate(data: String): MenuView {
        current = moveToNext(data) ?: current
        return MenuView(
            current.description,
            menuItemsToView(current.items),
            parentStack.lastOrNull()?.let { backView })
    }

    private fun moveToNext(data: String): MenuLayout? {
        return if (data == backCommand) {
            moveBack()
        } else {
            moveToSubMenu(data)
        }
    }

    private fun moveBack(): MenuLayout? {
        return parentStack.removeLastOrNull()
    }

    private fun moveToSubMenu(data: String): MenuLayout? {
        return current.items.find { it.name == data }?.let {
            it.action?.let { action -> executor.submit(action) }
            if (it.items.isEmpty()) {
                return@let null
            }
            parentStack.add(current)
            return@let it
        }
    }


    private fun menuItemsToView(items: List<MenuLayout>): List<MenuItemView> {
        return items.map { menuItemToView(it) }
    }

    private fun menuItemToView(item: MenuLayout): MenuItemView {
        return MenuItemView(item.name, id = item.name)
    }
}

class MenuNavigatorFactory(
    private val mainMenuTemplate: MenuLayout,
    private val executor: StateExecutor,
    private val messages: MessageResource,
) {
    fun mainMenuNavigator(): MenuNavigator {
        return MenuNavigator(mainMenuTemplate, executor)
    }
}