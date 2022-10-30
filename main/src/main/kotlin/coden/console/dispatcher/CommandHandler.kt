package coden.console.dispatcher

interface CommandHandler{
    fun handle(request: CommandRequest)
}