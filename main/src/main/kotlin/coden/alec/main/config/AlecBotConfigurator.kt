package coden.alec.main.config

import coden.alec.app.fsm.*
import coden.alec.app.menu.MenuPresenter
import coden.alec.bot.BotDispatcherConfigurator
import coden.alec.bot.config.CallbackQueryCapturingHandler
import coden.alec.bot.config.MessageCapturingHandler
import coden.alec.bot.context.ContextObserver
import coden.fsm.CommandExecutor
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text

class AlecBotConfigurator(
    private val context: ContextObserver,
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter
): BotDispatcherConfigurator {

    override fun Dispatcher.configure() {
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

        command("create_scale") {
            if (args.isEmpty()) {
                commandExecutor.submit(CreateScaleCommandNoArgs)
            } else {
                commandExecutor.submit(CreateScaleCommand(message.text!!))
            }
        }

        text {
            if (text.startsWith("/")) return@text
            commandExecutor.submit(TextCommand(text))
        }

        callbackQuery {
            callbackQuery.message?.let {
                menuExecutor.navigate(callbackQuery.data)?.let {
                    commandExecutor.submit(it)
                }
            }
        }
    }
}