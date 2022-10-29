package coden.console.view

import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay

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