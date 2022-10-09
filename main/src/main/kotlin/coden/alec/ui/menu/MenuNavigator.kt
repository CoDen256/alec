package coden.alec.ui.menu

import coden.fsm.Command

//
//class MenuNavigator (
//    private val menuLayout: MenuLayout,
//    backItemLayout: BackItemLayout
//){
//
//    private val backCommand = "MenuNavigator.BACK"
//    private val backView = MenuItemView(backItemLayout.description, id = backCommand)
//    private val parentStack = ArrayList<MenuLayout>()
//    private var current: MenuLayout = menuLayout
//
//    fun createMain(): MenuView {
//        return MenuView(
//            menuLayout.description,
//            menuItemsToView(menuLayout.items),
//            parentStack.lastOrNull()?.let { backView },
//        )
//    }
//
//
//    fun navigate(data: String): MenuView {
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
//    }
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
//    private fun menuItemsToView(items: List<MenuLayout>): List<MenuItemView> {
//        return items.map { menuItemToView(it) }
//    }
//
//    private fun menuItemToView(item: MenuLayout): MenuItemView {
//        return MenuItemView(item.name, id = item.name)
//    }
//}
//
//class MenuNavigatorFactory(
//    private val menuLayout: MenuLayout,
//    private val backItemLayout: BackItemLayout
//) {
//    fun mainMenuNavigator(): MenuNavigator {
//        return MenuNavigator(menuLayout, backItemLayout)
//    }
//}