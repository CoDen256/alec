package coden.alec.bot

import com.github.kotlintelegrambot.entities.ReplyMarkup

interface TelegramMessageSender {
    fun send(chatId: Long, message: TelegramMessage)
    fun edit(chatId: Long, messageId: Long, message: TelegramMessage)
}


data class TelegramMessage(val message: String, val replyMarkup: ReplyMarkup?)