package coden.alec.console

interface ConsoleDispatcher {
    fun command(command: String, handler: CommandRequest.() -> Unit){
        command(command, object: CommandHandler{
            override fun handle(request: CommandRequest) {
                request.handler()
            }
        })
    }
    fun command(command: String, handler: CommandHandler)
}

interface CommandHandler{
    fun handle(request: CommandRequest)
}

data class CommandRequest(
    val command: String,
    val args: String
)