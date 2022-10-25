package coden.alec.bot.view

import com.github.kotlintelegrambot.Bot

class ContextData {

    lateinit var context: Context

    fun update(context: Context){
        this.context = context
    }
}

class Context (val bot: Bot, val chatId: Long, val messageId: Long?)