package coden.alec.bot.menu

import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.bot.view.TelegramMenuFormatter
import coden.fsm.Command
import coden.menu.MenuView
import coden.menu.NavigationResult
import java.lang.IllegalArgumentException


class TelegramMenuNavigatorDirector(private val factory: MenuNavigatorFactory) {
    private val menus = HashMap<Long, TelegramMenuNavigator>()

    fun createNewMainMenu(): Pair<MenuView, (Long) -> Unit> {
        val controller = TelegramMenuNavigator(factory.mainMenuNavigator())
        val menu = controller.createMainMenu()
        return menu to { menus[it] = controller }
    }

    fun handleCommand(messageId: Long, data: String): Result<NavigationResult> {
        return menus[messageId]?.let { Result.success(it.navigate(data)) } ?:
                Result.failure(IllegalArgumentException("Message: with id $messageId does not exist"))
    }

}