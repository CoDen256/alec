package coden.alec.ui.menu

import java.lang.IllegalArgumentException
import java.util.*

class MenuContext(
    val menu: MenuView,
    val childrenLayouts : Map<String, ItemLayout>
)

class MenuNavigator(
    private val menuLayout: MenuLayout,
) {

    private val backItem = ItemView(menuLayout.backItem.name, id = UUID.randomUUID().toString())
    private val contextStack = ArrayList<MenuContext>()

    fun createMainMenu(): MenuView {
        val newContext = createContext(menuLayout.description, menuLayout.items)
        contextStack.add(newContext)
        return newContext.menu
    }

    fun navigate(data: String): NavigationResult {
        if (data == backItem.id) {
            return moveBack()
        }
        val action = contextStack.last().childrenLayouts[data]?.let {
            addSubMenuIfExists(it)
            it.action
        }

        return NavigationResult(contextStack.last().menu, action)
    }

    private fun addSubMenuIfExists(it: ItemLayout) {
        if (it.children.isNotEmpty())
            contextStack.add(createContext(it.description ?: it.name, it.children, backItem))
    }

    private fun moveBack(): NavigationResult {
        if (contextStack.size > 1) contextStack.removeLast()
        return NavigationResult(contextStack.last().menu, null)
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

class MenuNavigatorFactory(private val menuLayout: MenuLayout, ) {
    fun mainMenuNavigator(): MenuNavigator {
        return MenuNavigator(menuLayout)
    }
}