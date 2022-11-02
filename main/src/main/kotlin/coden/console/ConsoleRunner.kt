package coden.console

import coden.console.dispatcher.ConsoleDispatcherBuilder
import coden.console.dispatcher.ConsoleDispatcherConfigurator
import coden.console.read.CommandReader

class ConsoleRunner(
    private val reader: CommandReader,
    private val dispatcherBuilder: ConsoleDispatcherBuilder,
    private val configurator: ConsoleDispatcherConfigurator
){

    fun run() {
        configurator.apply(dispatcherBuilder)
        val dispatcher = dispatcherBuilder.build()
        dispatcher.init()
        while (true) {
            val request = reader.read() ?: break
            dispatcher.submit(request)
        }
    }
}

