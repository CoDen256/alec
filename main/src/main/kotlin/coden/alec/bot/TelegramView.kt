package coden.alec.bot

import coden.alec.app.views.View
import coden.alec.bot.utils.send
import coden.alec.main.Menu
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class TelegramView(
    private val ctx: TelegramContext,
    private val menu: Menu
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
