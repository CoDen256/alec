package coden.console.dispatcher

interface ConsoleDispatcherConfigurator {
    fun apply(builder: ConsoleDispatcherBuilder){
        builder.configure()
    }
    fun ConsoleDispatcherBuilder.configure()
}