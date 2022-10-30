package coden.console.dispatcher

interface ConsoleDispatcherConfigurator {
    fun apply(dispatcher: ConsoleDispatcherBuilder){
        dispatcher.configure()
    }
    fun ConsoleDispatcherBuilder.configure()
}