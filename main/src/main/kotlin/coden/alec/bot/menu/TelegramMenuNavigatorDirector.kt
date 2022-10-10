package coden.alec.bot.menu

import coden.alec.app.menu.MenuNavigatorFactory


class TelegramMenuNavigatorDirector(private val factory: MenuNavigatorFactory)
{
    private val menus = HashMap<Long, TelegramMenuNavigator>()

    fun createNewMenu(bot: Bot, message: Message){
        val controller = TelegramMenuNavigator(factory.mainMenuNavigator())
        val (text, replyMarkup) = controller.createMain()
        val id = bot.send(message, text, replyMarkup = replyMarkup).get().messageId
        menus[id] = controller
    }

    fun handleCommand(bot: Bot, message: Message, data: String): Command?{
        return menus[message.messageId]?.let {
            val (text, markup, action) = it.navigate(data)
            bot.edit(message, text=text, replyMarkup = markup)
            return action
        }
    }

}