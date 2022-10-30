package coden.console.dispatcher

interface ConsoleDispatcherBuilder {
    fun command(command: String, handler: CommandRequest.() -> Unit){
        command(command, object: CommandHandler {
            override fun handle(request: CommandRequest) {
                request.handler()
            }
        })
    }
    fun command(command: String, handler: CommandHandler)

    fun init(newInitBlock: () -> Unit)

}


