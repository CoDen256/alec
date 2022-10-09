package coden.menu

import coden.fsm.Command
import java.util.*


class MenuNavigator(private val menuLayout: MenuLayout) {

    private val backItem = ItemView(menuLayout.backItem.name, id = UUID.randomUUID().toString())
    private val contextStack = ArrayList<MenuContext>()

    fun createMainMenu(): MenuView {
        val newContext = createContext(menuLayout.description, menuLayout.items)
        contextStack.clear()
        contextStack.add(newContext)
        return newContext.menu
    }

    fun navigate(destination: String): NavigationResult {
        var action: Command? = null
        if (isDestinationUp(destination)){
            moveUp()
        } else {
            val currentLayout = contextStack.last().childrenLayouts[destination]
            action = currentLayout?.action
            moveDown(currentLayout)
        }

        return NavigationResult(contextStack.last().menu, action)
    }

    private fun isDestinationUp(data: String) = data == backItem.id

    private fun moveUp(){
        if (contextStack.size > 1) contextStack.removeLast()
    }

    private fun moveDown(it: ItemLayout?) {
        if (it != null && it.children.isNotEmpty())
            contextStack.add(createContext(it.description ?: it.name, it.children, backItem))
    }

    private fun createContext(description: String, items: List<ItemLayout>, backItem: ItemView? = null): MenuContext {
        val layouts = itemLayoutsToViews(items)
        return MenuContext(
            MenuView(description, layouts.map { it.second }, backItem),
            layouts.associate { it.second.id to it.first }
        )
    }

    private fun itemLayoutsToViews(items: List<ItemLayout>): List<Pair<ItemLayout, ItemView>> {
        return items.map { it to  itemLayoutToView(it)}
    }

    private fun itemLayoutToView(item: ItemLayout): ItemView {
        return ItemView(item.name, id = UUID.randomUUID().toString())
    }
}

class MenuContext(
    val menu: MenuView,
    val childrenLayouts : Map<String, ItemLayout>
)