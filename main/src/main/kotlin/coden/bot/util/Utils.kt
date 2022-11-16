package coden.bot.util

import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment

fun CommandHandlerEnvironment.arguments(): String {
    return message.text!!.split(" ", limit = 2)[1]
}