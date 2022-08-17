package coden.alec.bot

import coden.alec.bot.controllers.ListScalesController
import coden.alec.bot.controllers.ListScalesPresenter
import coden.alec.bot.controllers.StartController
import coden.alec.bot.messages.MessageResource
import coden.alec.interactors.definer.scale.ListScalesInteractor
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import gateway.memory.ScaleInMemoryGateway

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
            command("list_scales") {
                val activator = ListScalesInteractor(
                    ScaleInMemoryGateway(), ListScalesPresenter(bot, message)
                )
                ListScalesController(activator, bot).handle(message)
            }
        }
    }

    fun launch(){
        bot.startPolling()
    }
}