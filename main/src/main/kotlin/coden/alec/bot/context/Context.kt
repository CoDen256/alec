package coden.alec.bot.context

import com.github.kotlintelegrambot.Bot


class ContextObserver: () -> Context {

    private lateinit var context: Context

    fun update(context: Context){
        this.context = context
    }

    override fun invoke(): Context {
        return context
    }
}

class Context (val bot: Bot, val chatId: Long, val messageId: Long?)

class TelegramChatContext(val chatId: Long)
class TelegramMessageContext(val chatId: Long, val messageId: Long)