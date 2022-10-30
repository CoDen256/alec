package coden.alec.console

import coden.alec.app.AppRunner
import coden.console.dispatcher.ConsoleDispatcher
import coden.console.read.CommandReader

class ConsoleRunner(
    private val reader: CommandReader,
    private val dispatcher: ConsoleDispatcher
): AppRunner {

    override fun run() {
        dispatcher.init()
        while (true) {
            val request = reader.read() ?: break
            dispatcher.submit(request)
        }
    }
}

