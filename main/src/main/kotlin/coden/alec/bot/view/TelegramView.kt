package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.TelegramContext
import coden.alec.bot.utils.edit
import coden.alec.bot.utils.send
import coden.menu.MenuView
import com.github.kotlintelegrambot.entities.Message

class TelegramView(private val ctx: TelegramContext): View {

    private val formatter = TelegramMenuFormatter()

    override fun displayPrompt(message: String) {
        ctx.bot.send(ctx.current, message)
    }

    override fun displayMessage(message: String) {
        ctx.bot.send(ctx.current, message)
    }

    override fun displayMenu(menu: MenuView) {
        val response = formatter.format(menu)
        val current = ctx.current
        if (isMessageFromBot(current)){
            ctx.bot.edit(current, text = response.message, replyMarkup = response.replyMarkup)
        }else{
            ctx.bot.send(current, response.message, replyMarkup = response.replyMarkup)
        }

    }

    private fun isMessageFromBot(current: Message) = current.from?.isBot == true

    override fun displayError(message: String) {
        ctx.bot.send(ctx.current, message)
    }

}
