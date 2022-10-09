package coden.alec.console.view

import coden.alec.app.views.View

class ConsoleView: View {
    override fun displayPrompt(message: String) {
        println("Please add something: $message")
    }

    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayMainMenu(message: String) {
        println(message)
    }

    override fun displayError(message: String) {
        println("Error: $message")
    }

}