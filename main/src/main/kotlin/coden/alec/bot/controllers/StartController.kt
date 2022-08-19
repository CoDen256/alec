package coden.alec.bot.controllers

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.utils.send
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class StartController (
    private val bot: Bot,
    private val messageResource: MessageResource
    ) {

    fun handle(message: Message){
        sendStartMessage(bot, message)
    }

    fun sendStartMessage(bot: Bot, message: Message){

        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(text = "List Scales", callbackData = "listScales"),
                InlineKeyboardButton.CallbackData(text = "Create Scale", callbackData = "createScale")
            ),
        )
        bot.send(message, messageResource.startMessage, replyMarkup = inlineKeyboardMarkup)

    }

}