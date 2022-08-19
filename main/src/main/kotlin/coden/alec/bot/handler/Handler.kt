package coden.alec.bot.handler

interface Handler {
    fun handle(args: String): Boolean
}