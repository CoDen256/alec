package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.TelegramContext
import coden.alec.bot.utils.edit
import coden.alec.bot.utils.send
import coden.menu.MenuView

class TelegramView(
    private val ctx: TelegramContext,
): View {

    private val formatter = TelegramMenuFormatter()

    private val messageIds: List<Long> = ArrayList()

    override fun displayPrompt(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

    override fun displayMessage(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

    override fun displayMenu(mainMenu: MenuView) {
        val response = formatter.format(mainMenu)
        val lastMessage = ctx.lastMessage
        if (messageIds.contains(lastMessage.messageId)){
             ctx.bot.edit(lastMessage, text = response.message, replyMarkup = response.replyMarkup)
        }else{
            val msg = ctx.bot.send(lastMessage, response.message, replyMarkup = response.replyMarkup)
            ctx.lastMessage = msg.get()
        }

    }

    override fun displayError(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

}
