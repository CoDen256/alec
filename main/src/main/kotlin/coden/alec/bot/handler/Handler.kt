package coden.alec.bot.handler

import com.github.kotlintelegrambot.entities.Message

interface Handler {
    fun handleArguments(message: Message): Boolean
}