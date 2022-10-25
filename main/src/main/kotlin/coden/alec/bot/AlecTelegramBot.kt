package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.app.menu.MenuExecutor
import coden.alec.bot.menu.TelegramMenuExecutor
import coden.alec.bot.view.ContextData
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.logging.LogLevel

class AlecTelegramBot (
    botToken: String,
    log: LogLevel,
    private val context: ContextData,
    private val stateExecutor: StateExecutor,
    private val telegramMenuExecutor: MenuExecutor
) {

    private val bot = bot {
        token = botToken
        logLevel = log

        dispatch {

            addHandler(MessageCapturingHandler(context))
            addHandler(CallbackQueryCapturingHandler(context))

            command("help") {
                stateExecutor.submit(HelpCommand)
                telegramMenuExecutor.displayMenu()
            }

            command("start") {
                stateExecutor.submit(HelpCommand)
                telegramMenuExecutor.displayMenu()
            }

            command("list_scales") {
                stateExecutor.submit(ListScalesCommand)
            }

            command("create_scale"){
                if (args.isEmpty()){
                    stateExecutor.submit(CreateScaleCommandNoArgs)
                }else {
                    stateExecutor.submit(CreateScaleCommand(message.text!!))
                }
            }

            text {
                if (text.startsWith("/")) return@text
                stateExecutor.submit(TextCommand(text))
            }

            callbackQuery{
                callbackQuery.message?.let {
                    telegramMenuExecutor.navigate(callbackQuery.data)?.let {
                        stateExecutor.submit(it)
                    }
                }
            }
        }
    }



    fun launch(){
        bot.startPolling()
    }
}