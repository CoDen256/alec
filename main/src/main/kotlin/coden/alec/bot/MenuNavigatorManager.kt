package coden.alec.bot

import coden.alec.bot.utils.edit
import coden.alec.bot.utils.send
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

class MenuNavigatorManager
    (private val factory: MenuNavigatorFactory)
{
    private val menus = HashMap<Long, MenuNavigator>()

    fun createNewMenu(bot: Bot, message: Message){
        val controller = factory.mainMenuNavigator()
        val (text, replyMarkup) = controller.createMain()
        val id = bot.send(message, text, replyMarkup = replyMarkup).get().messageId
        menus[id] = controller
    }

    fun handleCommand(bot: Bot, message: Message, data: String){
        menus[message.messageId]?.let {
            val (text, markup) = it.navigate(data)
            bot.edit(message, text=text, replyMarkup = markup)
        }
    }

}