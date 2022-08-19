package coden.alec.bot.controllers

interface Controller {
    fun handle(args: Map<String, Any>)
}