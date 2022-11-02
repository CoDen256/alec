package coden.alec.console

import coden.alec.app.fsm.*
import coden.alec.app.resources.CommandNamesResource
import coden.console.dispatcher.ConsoleDispatcherConfigurator
import coden.console.dispatcher.ConsoleDispatcherBuilder
import coden.display.menu.MenuPresenter
import coden.fsm.CommandExecutor

class AlecConsoleConfigurator(
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter,
    private val commandNames: ConsoleCommandNamesResource
): ConsoleDispatcherConfigurator {
    override fun ConsoleDispatcherBuilder.configure(){
        init {
            menuExecutor.displayMenu()
            commandExecutor.submit(HelpCommand)
        }

        command(commandNames.startCommand) {
            commandExecutor.submit(HelpCommand)
            menuExecutor.displayMenu()
        }
        command(commandNames.helpCommand) {
            commandExecutor.submit(HelpCommand)
            menuExecutor.displayMenu()
        }

        command(commandNames.listScalesCommand) {
            commandExecutor.submit(ListScalesCommand)
        }

        command(commandNames.createScaleCommand) {
            if (args.isEmpty()) {
                commandExecutor.submit(CreateScaleCommandNoArgs)
            } else {
                commandExecutor.submit(CreateScaleCommand(args))
            }
        }
        command(commandNames.textCommand){
            commandExecutor.submit(TextCommand(args))
        }

        command(commandNames.navCommand){
            menuExecutor.navigate(args)?.let {
                commandExecutor.submit(it)
            }
        }
    }

}