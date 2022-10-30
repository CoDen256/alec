package coden.console.dispatcher

interface ConsoleDispatcher {
    fun submit(request: CommandRequest)
    fun init()
}