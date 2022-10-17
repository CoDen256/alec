package coden.alec.bot.sender

import com.github.kotlintelegrambot.entities.ReplyMarkup

data class TelegramMessage(
    val message: String,
    val replyMarkup: ReplyMarkup?
)