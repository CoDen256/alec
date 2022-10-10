package coden.alec.console.view

import coden.alec.app.views.View
import coden.menu.MenuView

class ConsoleView: View {

    private val formatter = ConsoleMenuFormatter()

    override fun displayPrompt(message: String) {
        println("Please add something: $message")
    }

    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayMenu(menu: MenuView) {
        println(formatter.format(menu))
    }

    override fun displayError(message: String) {
        println("Error: $message")
    }

}