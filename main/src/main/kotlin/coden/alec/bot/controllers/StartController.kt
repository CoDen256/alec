package coden.alec.bot.controllers

import coden.alec.bot.messages.MessageResource
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode

class StartController (
    private val bot: Bot,
    private val messageResource: MessageResource
    ) {

    fun handle(message: Message){
        sendStartMessage(bot, message)
    }

    fun sendStartMessage(bot: Bot, message: Message){
        bot.sendMessage(
            chatId = ChatId.fromId(message.chat.id),
            text = messageResource.startMessage,
            parseMode = ParseMode.MARKDOWN
            )
    }

}