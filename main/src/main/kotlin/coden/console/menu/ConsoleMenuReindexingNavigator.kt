package coden.console.menu

import coden.display.menu.MenuNavigator
import coden.menu.*
import java.lang.IllegalArgumentException

class ConsoleMenuReindexingNavigator(private val navigator: LayoutBasedMenuNavigator): MenuNavigator {

    private val destinationMapping = HashMap<String, String>()
    private var currentId = 0

    override fun createMainMenu(): MenuView {
        return reindexMenu(navigator.createMainMenu())
    }

    override fun navigate(destination: String): Result<NavigationResult> {
        val innerDestination = destinationMapping[destination]
            ?: return Result.failure(IllegalArgumentException("Selection $destination is invalid"))
        val result = navigator.navigate(innerDestination)
        return Result.success(NavigationResult(reindexMenu(result.menu), result.action))
    }

    private fun reindexMenu(menu: MenuView): MenuView {
        resetMapping()
        return MenuView(menu.description,
            menu.items.map { reindexMapItemAndSave(it) },
            backItemView =  menu.backItemView?.let { reindexMapItemAndSave(it) }
            )
    }

    private fun reindexMapItemAndSave(it: ItemView): ItemView {
        currentId++
        destinationMapping[currentId.toString()] = it.id
        return ItemView(it.name, currentId.toString())
    }

    private fun resetMapping() {
        destinationMapping.clear()
        currentId = 0
    }
}