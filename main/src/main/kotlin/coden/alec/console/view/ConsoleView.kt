package coden.alec.console.view

import coden.alec.app.views.MenuView
import coden.alec.app.views.View

class ConsoleView: View {

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


class ConsoleMenuView(private val formatter: ConsoleMenuFormatter): MenuView {
    override fun displayMenu(menu: coden.menu.MenuView) {
        println(formatter.format(menu))
    }
}