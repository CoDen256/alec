package coden.alec.bot

import coden.alec.bot.view.Context
import coden.alec.bot.view.ContextData
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.entities.Update

class MessageCapturingHandler (private val context: ContextData): Handler {
    override fun checkUpdate(update: Update): Boolean = update.message != null

    override fun handleUpdate(bot: Bot, update: Update) {
        update.message!!.let {
            context.update(Context(bot, it.chat.id, null))
        }
    }

}

class CallbackQueryCapturingHandler (private val context: ContextData): Handler {
    override fun checkUpdate(update: Update): Boolean = update.callbackQuery?.message != null

    override fun handleUpdate(bot: Bot, update: Update) {
        update.callbackQuery!!.message!!.let {
            context.update(Context(bot, it.chat.id, it.messageId))
        }
    }
}