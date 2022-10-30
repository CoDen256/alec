package coden.alec.console

import coden.alec.app.AppRunner
import coden.console.read.CommandReader
import coden.console.dispatcher.CommandHandler
import coden.console.dispatcher.ConsoleDispatcherBuilder

class ConsoleRunner(
    private val reader: CommandReader,
): AppRunner, ConsoleDispatcherBuilder {

    private val handlers: MutableMap<String, CommandHandler> = HashMap()
    private var initBlock: () -> Unit = {}

    override fun command(command: String, handler: CommandHandler) {
        handlers[command] = handler
    }

    override fun init(newInitBlock: () -> Unit) {
        initBlock = { // :D
            initBlock()
            newInitBlock()
        }
    }

    override fun run() {
        initBlock()
        while (true) {
            val request = reader.read() ?: break
            handlers[request.command]?.apply {
                handle(request)
            }
        }
    }
}

