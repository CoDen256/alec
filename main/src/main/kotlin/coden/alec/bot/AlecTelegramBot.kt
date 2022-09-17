package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.bot.utils.send
import coden.alec.main.Menu
import coden.alec.main.MenuItem
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import kotlin.collections.HashMap

class AlecTelegramBot (
    botToken: String,
    private val ctx: TelegramContext,
    private val stateExecutor: StateExecutor,
    private val menu: Menu
) {


    private val menus = HashMap<Long, MenuController>()

    private val bot = bot {
        token = botToken

        dispatch {

            command("help") {
                ctx.update(bot, lastMessage = message)
                stateExecutor.submit(HelpCommand)
            }

            command("start") {
                ctx.update(bot, lastMessage = message)
                stateExecutor.submit(HelpCommand)

                val controller = MenuController(menu, stateExecutor)
                val (text, replyMarkup) = controller.create()
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
                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                callbackQuery.message?.messageId?.let {messageId ->
                    menus[messageId]?.let {
                        val (text, markup) = it.submit(callbackQuery.data)
                        bot.editMessageText(ChatId.fromId(chatId), callbackQuery.message?.messageId, text=text, replyMarkup = markup )
                    }
                }
            }
        }
    }
    fun launch(){
        bot.startPolling()
    }
}

class MenuController (
    private val menu: Menu,
    private val executor: StateExecutor,
    private val itemsPerRow: Int = 4
){

    fun create(): Pair<String, ReplyMarkup> {
        return menu.description to menuToMarkup(menu.items)
    }


    fun submit(data: String): Pair<String, ReplyMarkup>{
        return "inlined" to
        InlineKeyboardMarkup.create(
                    listOf(InlineKeyboardButton.CallbackData(text = "Test Inline Button 2", callbackData = "testButton")),
                    listOf(InlineKeyboardButton.CallbackData(text = "Show alert 2", callbackData = "showAlert"))
                )
    }


    private fun menuToMarkup(items: List<MenuItem>): InlineKeyboardMarkup {
        val result = ArrayList<List<InlineKeyboardButton>>()
        generateSequence(0) { n -> n + itemsPerRow }
            .take((items.size + (itemsPerRow-1)) / itemsPerRow)
            .forEach { tupleIndex ->
                val row = ArrayList<InlineKeyboardButton>()
                (tupleIndex until (tupleIndex + itemsPerRow).coerceAtMost(items.size)).map {
                    row.add(menuItemToButton(items[it]))
                }
                result.add(row)
            }

        return InlineKeyboardMarkup.create(result)
    }

    private fun menuItemToButton(item: MenuItem): InlineKeyboardButton{
        return InlineKeyboardButton.CallbackData(item.name, callbackData = item.name)
    }
}