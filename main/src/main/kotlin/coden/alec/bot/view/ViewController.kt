package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.sender.BaseMessageSender
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView
import com.github.kotlintelegrambot.Bot

class ViewController(
    private val formatter: TelegramMenuFormatter,
): View {

    lateinit var context: Context

    private val sender: TelegramMessageSender by lazy {
        BaseMessageSender(context.bot)
    }

    private val view: View
        get() = context.messageId?.let {
            TelegramInlineView(TelegramMessageContext(context.chatId, it), sender, formatter)
        } ?: CommonTelegramView(TelegramChatContext(context.chatId), sender, formatter)



    override fun displayPrompt(message: String) {
        view.displayPrompt(message)
    }

    override fun displayMessage(message: String) {
        view.displayMessage(message)
    }

    override fun displayMenu(menu: MenuView) {
        view.displayMenu(menu)
    }

    override fun displayError(message: String) {
        view.displayError(message)
    }
}

class Context (val bot: Bot, val chatId: Long, val messageId: Long?)