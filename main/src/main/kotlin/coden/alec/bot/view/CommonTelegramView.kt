package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.sender.TelegramMessage
import coden.alec.bot.sender.TelegramMessageSender

class CommonTelegramView(
    private val context: TelegramChatContext,
    private val messageSender: TelegramMessageSender,
) : View {

    override fun displayPrompt(message: String) {
        messageSender.send(context.chatId, TelegramMessage(message, null))
    }

    override fun displayMessage(message: String) {
        messageSender.send(context.chatId, TelegramMessage(message, null))
    }

    override fun displayError(message: String) {
        messageSender.send(context.chatId, TelegramMessage(message, null))
    }

}
