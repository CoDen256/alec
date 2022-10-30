package coden.alec.console

interface ConsoleConfigurator {
    fun apply(dispatcher: ConsoleDispatcher){
        dispatcher.configure()
    }
    fun ConsoleDispatcher.configure()
    fun init()
}