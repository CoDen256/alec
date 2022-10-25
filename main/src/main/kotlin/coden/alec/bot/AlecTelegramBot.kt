package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.app.views.View
import coden.alec.bot.menu.TelegramMenuNavigatorDirector
import coden.alec.bot.view.ViewContextHolder
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
    private val mainView: View,
    private val menuView: View,
    private val context: ViewContextHolder,
    private val stateExecutor: StateExecutor,
    private val director: TelegramMenuNavigatorDirector
) {

    private val bot = bot {
        token = botToken
        logLevel = log

        dispatch {

            addHandler(MessageCapturingHandler(context))
            addHandler(CallbackQueryCapturingHandler(context))

            command("help") {
                stateExecutor.submit(HelpCommand)
            }

            command("start") {
                stateExecutor.submit(HelpCommand)

                mainView.displayMenu(director.createNewMainMenu())
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
                    director.handleCommand(callbackQuery.data).onSuccess { result ->
                        menuView.displayMenu(result.menu)
                        result.action?.let { action -> stateExecutor.submit(action) }
                    }.onFailure { throwable ->
                        throwable.message?.let { msg -> mainView.displayError(msg) }
                    }
                }
            }
        }
    }



    fun launch(){
        bot.startPolling()
    }
}