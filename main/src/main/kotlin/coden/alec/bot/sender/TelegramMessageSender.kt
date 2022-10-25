package coden.alec.bot.sender

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId

interface TelegramMessageSender {
    fun send(chatId: Long, message: TelegramMessage)
    fun edit(chatId: Long, messageId: Long, message: TelegramMessage)
}

class BaseMessageSender(private val bot: Bot) : TelegramMessageSender {
    override fun send(chatId: Long, message: TelegramMessage) {
        bot.sendMessage(ChatId.fromId(chatId), message.content, replyMarkup = message.replyMarkup)
    }

    override fun edit(chatId: Long, messageId: Long, message: TelegramMessage) {
        bot.editMessageText(ChatId.fromId(chatId), messageId= messageId, text = message.content, replyMarkup = message.replyMarkup)
    }

}