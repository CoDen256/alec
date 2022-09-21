package coden.alec.console

import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.main.Menu
import coden.fsm.StateExecutor

class ConsoleApp(
    private val view: ConsoleView,
    private val stateExecutor: StateExecutor,
    private val menu: Menu
    ) {

    fun start() {
        stateExecutor.submit(HelpCommand)
        while (true) {
            val input = readLine() ?: break
            if (input.startsWith("/")) {
                val args = input.drop(1).split(" ")
                val command = args[0]
//                val commandArgs = args.getLastOrNull()
                if (command == "help") {
                    stateExecutor.submit(HelpCommand)
                }
                if (command == "listScales") {
                    stateExecutor.submit(ListScalesCommand)
                }
            }
        }
    }

}