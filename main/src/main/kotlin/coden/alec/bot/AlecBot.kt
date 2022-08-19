package coden.alec.bot

import coden.alec.app.ListScalesController
import coden.alec.bot.handler.Handler
import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.TelegramView
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command

class AlecBot (
    private val scaleController: ListScalesController,
    private val view: TelegramView,
    botToken: String,
    messageResource: MessageResource
) {

    private var handler: Handler? = null
    private val bot = bot {
        token = botToken

        dispatch {
            command("list_scales") {
                view.update(bot, lastMessage = message)
                scaleController.handle()
            }

        }
    }
    fun launch(){
//
//        globalContext.add(
//            command("create_scale", CreateScaleHandler){
//                text (CreateScaleArgHandler) {
//
//                }
//            }
//            command("start", StartHandler)
//
//            text (TextHandler)
//        )
//
        bot.startPolling()
    }
}