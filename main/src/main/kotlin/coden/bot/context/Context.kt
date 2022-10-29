package coden.bot.context

import com.github.kotlintelegrambot.Bot


class ContextObserver: () -> Context {

    private var context: Context? = null

    fun update(context: Context){
        this.context = context
    }

    override fun invoke(): Context {
        return context ?: throw IllegalStateException("Context is not provided")
    }
}

class Context (val bot: Bot, val chatId: Long, val messageId: Long?)

class TelegramChatContext(val chatId: Long)
class TelegramMessageContext(val chatId: Long, val messageId: Long)