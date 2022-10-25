package coden.alec.bot.view

import com.github.kotlintelegrambot.Bot

class ViewContextHolder {

    lateinit var context: Context

    fun updateContext(context: Context){
        this.context = context
    }
}

class Context (val bot: Bot, val chatId: Long, val messageId: Long?)