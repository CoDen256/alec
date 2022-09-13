package coden.alec.bot

import coden.alec.app.FiniteStateMachine
import coden.fsm.StateExecutor
import coden.alec.app.commands.*
import coden.alec.app.fsm.*
import coden.alec.app.states.*
import coden.alec.bot.utils.send
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class AlecBot (
    private val view: TelegramView,
    private val inline: TelegramInlineView,
    botToken: String,
    private val stateExecutor: StateExecutor,
    private val messageFSM: FiniteStateMachine
) {


    private val stateExecutors: MutableMap<Long, StateExecutor> = HashMap()

    private val bot = bot {
        token = botToken

        dispatch {

            command("help") {
                view.update(bot, lastMessage = message)
                val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                    listOf(InlineKeyboardButton.CallbackData(text = "Test Inline Button", callbackData = "listScaleInline")),
                )
                val id = bot.send(message, "something", replyMarkup = inlineKeyboardMarkup).get().messageId
                stateExecutors[id] = StateExecutor(messageFSM)
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

            callbackQuery("listScaleInline") {
                val message = callbackQuery.message ?: return@callbackQuery
                val executor = stateExecutors[message.messageId]
                executor?.let {
                    inline.updateCallback(bot, botMessage = message)
                    it.submit(ListScalesInlineCommand)
                }
            }
        }
    }
    fun launch(){
        bot.startPolling()
    }
}