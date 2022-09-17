package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.bot.utils.edit
import coden.alec.bot.utils.send
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
    private val ctx: TelegramContext,
    private val stateExecutor: StateExecutor,
    private val factory: MenuControllerFactory
) {


    private val menus = HashMap<Long, MenuController>()

    private val bot = bot {
        token = botToken
        logLevel = log

        dispatch {

            command("help") {
                ctx.update(bot, lastMessage = message)
                stateExecutor.submit(HelpCommand)
            }

            command("start") {
                ctx.update(bot, lastMessage = message)
                stateExecutor.submit(HelpCommand)

                val controller = factory.controller()
                val (text, replyMarkup) = controller.createMain()
                val id = bot.send(message, text, replyMarkup = replyMarkup).get().messageId
                menus[id] = controller
            }

            command("list_scales") {
                ctx.update(bot, lastMessage = message)
                stateExecutor.submit(ListScalesCommand)
            }

            command("create_scale"){
                ctx.update(bot, lastMessage = message)
                if (args.isEmpty()){
                    stateExecutor.submit(CreateScaleCommandNoArgs)
                }else {
                    stateExecutor.submit(CreateScaleCommand(message.text!!))
                }
            }

            text {
                if (text.startsWith("/")) return@text
                ctx.update(bot, lastMessage = message)
                stateExecutor.submit(TextCommand(text))
            }

            callbackQuery{
                callbackQuery.message?.let {message ->
                    menus[message.messageId]?.let {
                        val (text, markup) = it.submit(callbackQuery.data)
                        bot.edit(message, text=text, replyMarkup = markup)
                    }
                }
            }
        }
    }
    fun launch(){
        bot.startPolling()
    }
}