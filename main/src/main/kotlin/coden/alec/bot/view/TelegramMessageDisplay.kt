package coden.alec.bot.view

import coden.alec.app.views.MessageDisplay
import coden.alec.bot.sender.TelegramMessage
import coden.alec.bot.sender.TelegramMessageSender

class TelegramMessageDisplay(
    private val context: TelegramChatContext,
    private val messageSender: TelegramMessageSender,
) : MessageDisplay {

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
