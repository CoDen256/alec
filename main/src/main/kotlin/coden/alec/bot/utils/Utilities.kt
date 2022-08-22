package coden.alec.bot.utils

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.types.TelegramBotResult

fun Bot.send(message: Message,
             text: String,
             parseMode: ParseMode = ParseMode.MARKDOWN,
             replyMarkup: ReplyMarkup? = null
): TelegramBotResult<Message> =
    sendMessage(
        chatId = ChatId.fromId(message.chat.id),
        text = text,
        parseMode = parseMode,
        replyMarkup = replyMarkup
    )


fun Bot.edit(message: Message,
             text: String,
             parseMode: ParseMode = ParseMode.MARKDOWN,
             replyMarkup: ReplyMarkup? = null
){
    editMessageText(
        chatId = ChatId.fromId(message.chat.id),
        messageId = message.messageId,
        text = text,
        parseMode = parseMode,
        replyMarkup = replyMarkup
    )
}