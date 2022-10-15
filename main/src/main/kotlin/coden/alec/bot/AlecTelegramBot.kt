//package coden.alec.bot
//
//import coden.alec.app.fsm.*
//import coden.alec.bot.menu.TelegramMenuNavigatorDirector
//import coden.alec.bot.view.TelegramContext
//import coden.alec.bot.view.TelegramView
//import coden.fsm.StateExecutor
//import com.github.kotlintelegrambot.bot
//import com.github.kotlintelegrambot.dispatch
//import com.github.kotlintelegrambot.dispatcher.callbackQuery
//import com.github.kotlintelegrambot.dispatcher.command
//import com.github.kotlintelegrambot.dispatcher.text
//import com.github.kotlintelegrambot.logging.LogLevel
//
//class AlecTelegramBot (
//    botToken: String,
//    log: LogLevel,
//    private val telegramView: TelegramView,
//    private val ctx: TelegramContext,
//    private val stateExecutor: StateExecutor,
//    private val manager: TelegramMenuNavigatorDirector
//) {
//
//
//    private val bot = bot {
//        token = botToken
//        logLevel = log
//
//        dispatch {
//
//            command("help") {
//                ctx.update(bot, current = message)
//                stateExecutor.submit(HelpCommand)
//            }
//
//            command("start") {
//                ctx.update(bot, current = message)
//                stateExecutor.submit(HelpCommand)
//
//                telegramView.displayMenu(manager.createNewMainMenu())
//            }
//
//            command("list_scales") {
//                ctx.update(bot, current = message)
//                stateExecutor.submit(ListScalesCommand)
//            }
//
//            command("create_scale"){
//                ctx.update(bot, current = message)
//                if (args.isEmpty()){
//                    stateExecutor.submit(CreateScaleCommandNoArgs)
//                }else {
//                    stateExecutor.submit(CreateScaleCommand(message.text!!))
//                }
//            }
//
//            text {
//                if (text.startsWith("/")) return@text
//                ctx.update(bot, current = message)
//                stateExecutor.submit(TextCommand(text))
//            }
//
//            callbackQuery{
//                callbackQuery.message?.let {
//                    ctx.update(bot, current = it)
//                    manager.handleCommand(callbackQuery.data).onSuccess {result ->
//                        telegramView.displayMenu(result.menu)
//                        result.action?.let { action -> stateExecutor.submit(action) }
//                    }.onFailure { throwable ->
//                        throwable.message?.let { msg -> telegramView.displayError(msg) }
//                    }
//                }
//            }
//        }
//    }
//    fun launch(){
//        bot.startPolling()
//    }
//}