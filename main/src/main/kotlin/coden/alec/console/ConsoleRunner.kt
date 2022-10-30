package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.app.AppRunner
import coden.display.menu.MenuPresenter
import coden.fsm.CommandExecutor
import java.util.regex.Pattern

class ConsoleRunner(
    private val commandExecutor: CommandExecutor,
    private val menuExecutor: MenuPresenter
): AppRunner {

    override fun run() {
        init()
        while (true) {
            val input = readInputOrNull() ?: break
            when{
                isCommand(input) -> {
                    val (command, args) = extractCommand(input)
                    handleCommand(command, args)
                }
                isText(input) -> handleText(extractText(input))
                isMenuNavigation(input) -> handleMenuNavigation(extractMenuNavigation(input))
            }
        }
    }

    private fun readInputOrNull(): String? = readlnOrNull()

    fun init() {
        menuExecutor.displayMenu()
        commandExecutor.submit(HelpCommand)
    }

    fun extractCommand(input: String): Pair<String, String?> {
        return input.drop(1) to input.split(" ", limit = 2)[1].ifEmpty { null }
    }

    fun extractText(input: String): String{
        return input.split(" ", limit = 2)[1]
    }

    fun extractMenuNavigation(input: String): String{
        return input
    }

    fun isCommand(input: String): Boolean{
        return input.matches(Pattern.compile("/\\w+").toRegex()) && !input.startsWith("/text")
    }

    fun isText(input: String): Boolean{
        return input.startsWith("/text")
    }

    fun isMenuNavigation(input: String): Boolean{
        return input.matches(Pattern.compile("/\\d+").toRegex())
    }

    fun handleCommand(command: String, args: String?){
        if (command == "help"){
            commandExecutor.submit(HelpCommand)
        }
    }

    fun handleText(text: String){
        commandExecutor.submit(TextCommand(text))
    }

    fun handleMenuNavigation(destination: String){
        menuExecutor.navigate(destination)?.let {
            commandExecutor.submit(it)
        }
    }
}

