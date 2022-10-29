package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.app.menu.MenuPresenter
import coden.alec.bot.context.ContextObserver
import coden.fsm.CommandExecutor
import coden.fsm.StateBasedCommandExecutor
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.logging.LogLevel

class AlecTelegramBot (
    botToken: String,
    log: LogLevel,
    private val context: ContextObserver,
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter
) {

    private val bot = bot {
        token = botToken
        logLevel = log

        dispatch {

            addHandler(MessageCapturingHandler(context))
            addHandler(CallbackQueryCapturingHandler(context))

            command("help") {
                commandExecutor.submit(HelpCommand)
                menuExecutor.displayMenu()
            }

            command("start") {
                commandExecutor.submit(HelpCommand)
                menuExecutor.displayMenu()
            }

            command("list_scales") {
                commandExecutor.submit(ListScalesCommand)
            }

            command("create_scale"){
                if (args.isEmpty()){
                    commandExecutor.submit(CreateScaleCommandNoArgs)
                }else {
                    commandExecutor.submit(CreateScaleCommand(message.text!!))
                }
            }

            text {
                if (text.startsWith("/")) return@text
                commandExecutor.submit(TextCommand(text))
            }

            callbackQuery{
                callbackQuery.message?.let {
                    menuExecutor.navigate(callbackQuery.data)?.let {
                        commandExecutor.submit(it)
                    }
                }
            }
        }
    }



    fun launch(){
        bot.startPolling()
    }
}