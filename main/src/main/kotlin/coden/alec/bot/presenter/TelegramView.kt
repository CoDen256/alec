package coden.alec.bot.presenter

import coden.alec.bot.utils.edit
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

}


class TelegramInlineView: View {

    private lateinit var bot: Bot
    private lateinit var botMessage: Message
    private lateinit var lastMessage: Message

    fun update(bot: Bot, lastMessage: Message){
        this.bot = bot
        this.lastMessage = lastMessage
    }

    fun updateCallback(bot: Bot, botMessage: Message){
        this.bot = bot
        this.botMessage = botMessage
    }

    override fun displayPrompt(message: String) {
        bot.send(lastMessage, message)
    }

    override fun displayMessage(message: String) {
        bot.edit(botMessage, message)
    }

    override fun displayError(message: String) {
        bot.send(lastMessage, message)
    }

}