package coden.alec.bot

import coden.alec.app.states.*
import coden.alec.bot.handler.Handler
import coden.alec.bot.presenter.TelegramView
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text

class AlecBot (
    private val view: TelegramView,
    botToken: String,
    private val stateExecutor: StateExecutor
) {

    private val bot = bot {
        token = botToken

        dispatch {

            command("help") {
                view.update(bot, lastMessage = message)
                stateExecutor.submit(HelpCommand)
            }

            command("start") {
                view.update(bot, lastMessage = message)
                stateExecutor.submit(HelpCommand)
            }

            command("list_scales") {
                view.update(bot, lastMessage = message)
                stateExecutor.submit(ListScalesCommand)
            }

            command("create_scale"){
                view.update(bot, lastMessage = message)
                if (args.isEmpty()){
                    stateExecutor.submit(CreateScaleCommandNoArgs)
                }else {
                    stateExecutor.submit(CreateScaleCommand(message.text!!))
                }
            }

            text {
                if (text.startsWith("/")) return@text
                view.update(bot, lastMessage = message)
                stateExecutor.submit(TextCommand(text))
            }

        }
    }
    fun launch(){
        bot.startPolling()
    }
}