package coden.alec.ui.menu

import java.util.UUID


class MenuNavigator(
    private val menuLayout: MenuLayout,
) {

    private val backCommand = "MenuNavigator.BACK"
    private val backView = ItemView(menuLayout.backItem.name, id = backCommand)
    private val parentStack = ArrayList<MenuLayout>()
    private lateinit var previous: MenuView
    private val layouts = HashMap<String, ItemLayout>()

    fun createMainMenu(): MenuView {
        // parentStack.lastOrNull()?.let { backView }
        return MenuView(
            menuLayout.description,
            itemLayoutsToViews(menuLayout.items),
            null
        ).also {
            previous = it
        }
    }

    //
//
    fun navigate(data: String): NavigationResult {
        return (layouts[data]?.let {
            val targetAction = it.action

            if (it.children.isEmpty()){
                return NavigationResult(
                    previous,
                    targetAction,
                )
            }
            val newMenu = MenuView(
                it.description ?: it.name,
                itemLayoutsToViews(it.children),
                backView
            )
            return NavigationResult(
                newMenu,
                targetAction,
            )
        } ?: NavigationResult(MenuView(
            menuLayout.description, itemLayoutsToViews(menuLayout.items), null),
            null
        )).also {
            previous = it.menu
        }



//        val next = moveToNext(data)
//        var action: Command? = null
//        next?.let {
//            current = next.first ?: current
//            action = next.second
//        }
//        return MenuView(
//            current.description,
//            menuItemsToView(current.items),
//            parentStack.lastOrNull()?.let { backView },
//            action
//        )
    }
//
//    private fun moveToNext(data: String): Pair<MenuLayout?, Command?>? {
//        return if (data == backCommand) {
//            moveBack()
//        } else {
//            moveToSubMenu(data)
//        }
//    }
//
//    private fun moveBack(): Pair<MenuLayout?, Command?>? {
//        return parentStack.removeLastOrNull()?.let {
//            return it to null
//        }
//    }
//
//    private fun moveToSubMenu(data: String): Pair<MenuLayout?, Command?>? {
//        return current.items.find { it.name == data }?.let {
//            if (it.items.isEmpty()) {
//                return@let Pair(null, it.action)
//            }
//            parentStack.add(current)
//            return@let Pair(it, it.action)
//        }
//    }
//
//


    private fun itemLayoutsToViews(items: List<ItemLayout>): List<ItemView> {
        return items.map { menuItemToView(it) }
    }

    private fun menuItemToView(item: ItemLayout): ItemView {
        return ItemView(item.name, id = UUID.randomUUID().toString()).also {
            layouts[it.id] = item
        }
    }
}

//class MenuNavigatorFactory(
//    private val menuLayout: MenuLayout,
//    private val backItemLayout: BackItemLayout
//) {
//    fun mainMenuNavigator(): MenuNavigator {
//        return MenuNavigator(menuLayout, backItemLayout)
//    }
//}