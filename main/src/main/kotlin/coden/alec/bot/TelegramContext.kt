package coden.alec.bot

import coden.alec.bot.utils.send
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

class TelegramContext {

    lateinit var bot: Bot
    lateinit var lastMessage: Message

    fun update(bot: Bot, lastMessage: Message){
        this.bot = bot
        this.lastMessage = lastMessage
    }
}