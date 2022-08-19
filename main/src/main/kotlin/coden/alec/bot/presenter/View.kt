package coden.alec.bot.presenter

interface View {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
    fun displayError(message: String)
    fun replyToMessage(message: String)
}