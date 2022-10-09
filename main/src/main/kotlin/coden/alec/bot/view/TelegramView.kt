package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.TelegramContext
import coden.alec.bot.utils.send
import coden.menu.MenuLayout

class TelegramView(
    private val ctx: TelegramContext,
    private val menu: MenuLayout
): View {

    override fun displayPrompt(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

    override fun displayMessage(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

    override fun displayMainMenu(message: String) {

        println("")
    }

    override fun displayError(message: String) {
        ctx.bot.send(ctx.lastMessage, message)
    }

}
