package coden.alec.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

class TelegramContext {

    lateinit var bot: Bot
    lateinit var current: Message

    fun update(bot: Bot, current: Message){
        this.bot = bot
        this.current = current
    }
}