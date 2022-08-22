package coden.alec.bot.presenter

import coden.alec.bot.utils.send
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

class TelegramView: View {

    private lateinit var bot: Bot
    private lateinit var lastMessage: Message;

    fun update(bot: Bot, lastMessage: Message){
        this.bot = bot
        this.lastMessage = lastMessage
    }


    override fun displayPrompt(message: String) {
        bot.send(lastMessage, message)
    }

    override fun displayMessage(message: String) {
        bot.send(lastMessage, message)
    }

    override fun displayError(message: String) {
        bot.send(lastMessage, message)
    }

    override fun replyToMessage(message: String) {
        bot.send(lastMessage, message)
    }
}