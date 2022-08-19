package coden.alec.bot

import coden.alec.bot.controllers.*
import coden.alec.bot.handler.Handler
import coden.alec.bot.messages.MessageResource
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.inlineQuery
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import gateway.memory.ScaleInMemoryGateway

class AlecBot (
    botToken: String,
    messageResource: MessageResource
) {

    private val gateway = ScaleInMemoryGateway()
    private var handler: Handler? = null
    private val bot = bot {
        token = botToken

        dispatch {
            command("start") {
                StartController(bot, messageResource).handle(message)
            }
            command("help") {
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
                val ctronroller = CreateScaleController(activator, bot)
                val controllerhandler = CreateScaleHandler(
                    bot = bot,
                    controller = ctronroller,
                )
                val handled = controllerhandler.handle(message, args)
                if (!handled) {
                    handler =  controllerhandler
                }
            }

            callbackQuery("listScales") {
                callbackQuery.message?.chat?.id ?: return@callbackQuery
                val activator = ListScalesInteractor(
                    gateway, ListScalesInlinePresenter(bot, callbackQuery.message!!)
                )
                ListScalesController(activator, bot).handle(callbackQuery.message!!)

            }

//            callbackQuery("createScale") {
//                callbackQuery.message?.chat?.id ?: return@callbackQuery
//                val activator = CreateScaleInteractor(
//                    gateway, CreateScaleInlinePresenter(bot, callbackQuery.message!!)
//                )
//                CreateScaleController(activator, bot).handle(callbackQuery.message!!, listOf())
//
//            }

            text {
                val handled = handler?.handleArguments(message)
                if (handled == true){
                    handler = null
                }
            }

        }
    }
    fun launch(){
        bot.startPolling()
    }
}