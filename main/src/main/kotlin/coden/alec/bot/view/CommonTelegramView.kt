package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.sender.TelegramMessage
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView

class CommonTelegramView(
    private val context: TelegramChatContext,
    private val messageSender: TelegramMessageSender,
    private val formatter: TelegramMenuFormatter,
) : View {

    override fun displayPrompt(message: String) {
        messageSender.send(context.chatId, TelegramMessage(message, null))
    }

    override fun displayMessage(message: String) {
        messageSender.send(context.chatId, TelegramMessage(message, null))
    }

    override fun displayMenu(menu: MenuView) {
        val telegramMessage = formatter.format(menu)
        messageSender.send(context.chatId, telegramMessage)
    }

    override fun displayError(message: String) {
        messageSender.send(context.chatId, TelegramMessage(message, null))
    }

}
