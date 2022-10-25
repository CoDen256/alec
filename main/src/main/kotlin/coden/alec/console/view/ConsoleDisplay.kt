package coden.alec.console.view

import coden.alec.app.views.MenuDisplay
import coden.alec.app.views.MessageDisplay

class ConsoleDisplay: MessageDisplay {

    override fun displayPrompt(message: String) {
        println("Please add something: $message")
    }

    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayError(message: String) {
        println("Error: $message")
    }
}


class ConsoleMenuDisplay(private val formatter: ConsoleMenuFormatter): MenuDisplay {
    override fun displayMenu(menu: coden.menu.MenuView) {
        println(formatter.format(menu))
    }
}