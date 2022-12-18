package coden.alec.bot

import coden.alec.app.fsm.*
import coden.alec.app.resources.CommandNamesResource
import coden.display.menu.MenuPresenter
import coden.bot.BotDispatcherConfigurator
import coden.bot.config.CallbackQueryCapturingHandler
import coden.bot.config.MessageCapturingHandler
import coden.bot.context.ContextObserver
import coden.bot.util.arguments
import coden.fsm.CommandExecutor
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text

class AlecBotConfigurator(
    private val context: ContextObserver,
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter,
    private val commandNamesResource: CommandNamesResource
): BotDispatcherConfigurator {

    override fun Dispatcher.configure() {
        addHandler(MessageCapturingHandler(context))
        addHandler(CallbackQueryCapturingHandler(context))

        command(commandNamesResource.helpCommand) {
            commandExecutor.submit(HelpCommand)
            menuExecutor.displayMenu()
        }

        command(commandNamesResource.startCommand) {
            commandExecutor.submit(HelpCommand)
            menuExecutor.displayMenu()
        }

        command(commandNamesResource.listScalesCommand) {
            commandExecutor.submit(ListScalesCommand)
        }

        command(commandNamesResource.createScaleCommand) {
            if (args.isEmpty()) {
                commandExecutor.submit(CreateScaleCommandNoArgs)
            } else {
                commandExecutor.submit(CreateScaleCommand(arguments()))
            }
        }

        command(commandNamesResource.deleteScaleCommand){
            if (args.isEmpty()){
                commandExecutor.submit(DeleteScaleCommandNoArgs)
            }else {
                commandExecutor.submit(DeleteScaleCommand(arguments()))
            }
        }

        command(commandNamesResource.purgeScaleCommand){
            if (args.isEmpty()){
                commandExecutor.submit(PurgeScaleCommandNoArgs)
            }else{
                commandExecutor.submit(PurgeScaleCommand(arguments()))
            }
        }

        command(commandNamesResource.updateNameScaleCommand){
            if (args.isEmpty()){
                commandExecutor.submit(UpdateScaleNameCommandNoArgs)
            }else{
                commandExecutor.submit(UpdateScaleNameCommand(arguments()))
            }
        }

        command(commandNamesResource.updateUnitScaleCommand){
            if (args.isEmpty()){
                commandExecutor.submit(UpdateScaleUnitCommandNoArgs)
            }else{
                commandExecutor.submit(UpdateScaleUnitCommand(arguments()))
            }
        }

        command(commandNamesResource.updateDivisionsScaleCommand){
            if (args.isEmpty()){
                commandExecutor.submit(UpdateScaleDivisionsCommandNoArgs)
            }else{
                commandExecutor.submit(UpdateScaleDivisionsCommand(arguments()))
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