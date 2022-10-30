package coden.alec.console

import coden.alec.app.fsm.*
import coden.display.menu.MenuPresenter
import coden.fsm.CommandExecutor

class AlecConsoleConfigurator(
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter
): ConsoleConfigurator {
    override fun ConsoleDispatcher.configure(){
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
                commandExecutor.submit(CreateScaleCommand(args))
            }
        }
        command("text"){
            commandExecutor.submit(TextCommand(args))
        }

        command("nav"){
            menuExecutor.navigate(args)?.let {
                commandExecutor.submit(it)
            }
        }
    }

    override fun init(){
        menuExecutor.displayMenu()
        commandExecutor.submit(HelpCommand)
    }
}