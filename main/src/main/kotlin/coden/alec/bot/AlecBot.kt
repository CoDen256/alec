package coden.alec.bot

import coden.alec.bot.controllers.StartController
import coden.alec.bot.messages.MessageResource
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command

class AlecBot (
    botToken: String,
    messageResource: MessageResource
) {

    private val bot = bot {
        token = botToken

        dispatch {
            command("start") {
                StartController(bot, messageResource).handle(message)
            }
        }
    }

    fun launch(){
        bot.startPolling()
    }
}