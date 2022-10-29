package coden.alec.bot.view.display

import coden.alec.app.views.MessageDisplay
import coden.alec.bot.sender.TelegramMessage
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.context.TelegramChatContext
import coden.alec.bot.context.TelegramMessageContext

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


class TelegramInlineMessageDisplay(
    private val context: TelegramMessageContext,
    private val messageSender: TelegramMessageSender,
) : MessageDisplay {

    override fun displayPrompt(message: String) {
        messageSender.edit(context.chatId, context.messageId, TelegramMessage(message, null))
    }

    override fun displayMessage(message: String) {
        messageSender.edit(context.chatId, context.messageId, TelegramMessage(message, null))
    }

    override fun displayError(message: String) {
        messageSender.edit(context.chatId, context.messageId, TelegramMessage(message, null))
    }

}