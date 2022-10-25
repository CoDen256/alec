package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.bot.menu.TelegramMenuNavigatorDirector
import coden.alec.bot.view.*
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.Update
import com.github.kotlintelegrambot.logging.LogLevel

class AlecTelegramBot (
    botToken: String,
    log: LogLevel,
    private val viewController: ViewController,
    private val stateExecutor: StateExecutor,
    private val director: TelegramMenuNavigatorDirector
) {

    private val bot = bot {
        token = botToken
        logLevel = log

        dispatch {

            addHandler(object: Handler{
                override fun checkUpdate(update: Update): Boolean {
                    return update.message != null
                }

                override fun handleUpdate(bot: Bot, update: Update) {
                    viewController.context = Context(bot, update.message!!.chat.id, null)
                }

            })

            addHandler(object: Handler{
                override fun checkUpdate(update: Update): Boolean {
                    return update.callbackQuery?.message != null
                }

                override fun handleUpdate(bot: Bot, update: Update) {
                    viewController.context = Context(bot, update.callbackQuery!!.message!!.chat.id,
                    update.callbackQuery!!.message!!.messageId
                        )
                }

            })

            command("help") {
                stateExecutor.submit(HelpCommand)
            }

            command("start") {
                stateExecutor.submit(HelpCommand)

                viewController.displayMenu(director.createNewMainMenu())
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
                        viewController.displayMenu(result.menu)
                        viewController.context = Context(bot, it.chat.id, null)

                        result.action?.let { action -> stateExecutor.submit(action) }
                    }.onFailure { throwable ->
                        throwable.message?.let { msg -> viewController.displayError(msg) }
                    }
                }
            }
        }
    }



    fun launch(){
        bot.startPolling()
    }
}