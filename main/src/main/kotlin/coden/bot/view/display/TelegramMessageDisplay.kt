package coden.bot.view.display

import coden.display.displays.MessageDisplay
import coden.bot.sender.TelegramMessage
import coden.bot.sender.TelegramMessageSender
import coden.bot.context.TelegramChatContext
import coden.bot.context.TelegramMessageContext

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