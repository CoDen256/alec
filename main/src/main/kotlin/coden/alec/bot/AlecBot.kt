package coden.alec.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId

class AlecBot (val token: String) {

    fun launch(){
        val bot = bot {
            token = this@AlecBot.token
            dispatch {
                text {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = text)
                }
            }
        }
        bot.startPolling()
    }
}