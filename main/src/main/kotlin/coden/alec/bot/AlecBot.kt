package coden.alec.bot

import coden.alec.bot.controllers.*
import coden.alec.bot.messages.MessageResource
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import gateway.memory.ScaleInMemoryGateway

class AlecBot (
    botToken: String,
    messageResource: MessageResource
) {

    private val gateway = ScaleInMemoryGateway()

    private val bot = bot {
        token = botToken

        dispatch {
            command("start") {
                StartController(bot, messageResource).handle(message)
            }
            command("list_scales") {
                val activator = ListScalesInteractor(
                    gateway, ListScalesPresenter(bot, message)
                )
                ListScalesController(activator, bot).handle(message)
            }

            command("create_scale") {
                val activator = CreateScaleInteractor(
                    gateway, CreateScalePresenter(bot, message)
                )
                CreateScaleController(activator, bot).handle(message, args)
            }
        }
    }

    fun launch(){
        bot.startPolling()
    }
}