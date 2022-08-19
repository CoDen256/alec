package coden.alec.bot.controllers

interface GlobalPresenter {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
    fun replyToMessage(message: String)
}