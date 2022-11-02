package coden.console

import coden.console.dispatcher.ConsoleDispatcherBuilder
import coden.console.dispatcher.ConsoleDispatcherConfigurator
import coden.console.read.CommandParser
import coden.console.read.CommandReader
import coden.display.displays.ErrorDisplay
import java.lang.Exception

class ConsoleRunner(
    private val parser: CommandParser,
    private val reader: CommandReader,
    private val dispatcherBuilder: ConsoleDispatcherBuilder,
    private val configurator: ConsoleDispatcherConfigurator,
    private val errorDisplay: ErrorDisplay
){

    fun run() {
        dispatcherBuilder.clear()
        configurator.apply(dispatcherBuilder)
        val dispatcher = dispatcherBuilder.build()
        dispatcher.init()
        while (true) {
            try{
                val request = parser.parseCommand(reader.readNextCommand()) ?: break
                dispatcher.submit(request)
            } catch (e: Exception){
                e.message?.let { errorDisplay.displayError(it) } ?: errorDisplay.displayError("Unknown error")
            }
        }
    }
}

