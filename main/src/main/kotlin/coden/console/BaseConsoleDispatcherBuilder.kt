package coden.console

import coden.console.dispatcher.CommandHandler
import coden.console.dispatcher.CommandRequest
import coden.console.dispatcher.ConsoleDispatcher
import coden.console.dispatcher.ConsoleDispatcherBuilder

class BaseConsoleDispatcherBuilder: ConsoleDispatcherBuilder {

    private val handlers: MutableMap<String, CommandHandler> = HashMap()
    private var initBlock: () -> Unit = {}

    override fun command(command: String, handler: CommandHandler) {
        handlers[command] = handler
    }

    override fun init(newInitBlock: () -> Unit) {
        val prevBlock = initBlock
        initBlock = { // :D
            prevBlock()
            newInitBlock()
        }
    }
    override fun build(): ConsoleDispatcher {
        return BaseDispatcher(handlers, initBlock)
    }

    override fun clear() {
        handlers.clear()
        initBlock = {}
    }

    private inner class BaseDispatcher(private val handlers: Map<String, CommandHandler>, private val initBlock: () -> Unit): ConsoleDispatcher {
        override fun submit(request: CommandRequest) {
            handlers[request.command]?.apply {
                handle(request)
            }
        }

        override fun init() {
            initBlock()
        }
    }
}

