package coden.alec.bot.menu

import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.bot.utils.edit
import coden.alec.bot.utils.send
import coden.alec.bot.view.TelegramMenuFormatter
import coden.fsm.Command
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message


class TelegramMenuNavigatorDirector(private val factory: MenuNavigatorFactory) {
    private val menus = HashMap<Long, TelegramMenuNavigator>()
    private val formatter = TelegramMenuFormatter()

    fun createNewMainMenu(bot: Bot, message: Message) {
        val controller = TelegramMenuNavigator(factory.mainMenuNavigator())
        val response = formatter.format(controller.createMainMenu())
        val id = bot.send(message, response.message, replyMarkup = response.replyMarkup).get().messageId
        menus[id] = controller
    }

    fun handleCommand(bot: Bot, message: Message, data: String): Command? {
        return menus[message.messageId]?.let {
            val navResult = it.navigate(data)
            val result = formatter.format(navResult.menu)
            bot.edit(message, text = result.message, replyMarkup = result.replyMarkup)
            return navResult.action
        }
    }

}