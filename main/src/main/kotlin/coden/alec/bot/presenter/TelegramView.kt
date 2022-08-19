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
        TODO("Not yet implemented")
    }

    override fun displayMessage(message: String) {
        bot.send(this.lastMessage, message)
    }

    override fun displayError(message: String) {
        TODO("Not yet implemented")
    }

    override fun replyToMessage(message: String) {
        TODO("Not yet implemented")
    }
}