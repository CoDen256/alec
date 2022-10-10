package coden.alec.app.views

import coden.menu.MenuView

interface View {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
    fun displayMenu(menu: MenuView)
    fun displayError(message: String)
}