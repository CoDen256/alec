package coden.alec.bot

import coden.alec.app.views.View
import coden.alec.bot.utils.send

class TelegramView(private val ctx: TelegramContext): View {

    override fun displayPrompt(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

    override fun displayMessage(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

    override fun displayError(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

}
