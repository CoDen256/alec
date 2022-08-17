package coden.alec.bot.utils

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode

fun Bot.send(message: Message, text: String){
    sendMessage(
        chatId = ChatId.fromId(message.chat.id),
        text = text,
        parseMode = ParseMode.MARKDOWN
    )
}