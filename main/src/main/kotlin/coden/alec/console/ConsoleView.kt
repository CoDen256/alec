package coden.alec.console

import coden.alec.bot.presenter.View

class ConsoleView: View {
    override fun displayPrompt(message: String) {
        TODO("Not yet implemented")
    }

    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayError(message: String) {
        println("Error: $message")
    }

    override fun replyToMessage(message: String) {
        TODO("Not yet implemented")
    }
}