package coden.alec.bot.view

import coden.alec.app.views.View
import coden.menu.MenuView

class ViewController(
    private val contextHolder: ViewContextHolder,
    private val viewFactory: (Context) -> View
): View {

    override fun displayPrompt(message: String) {
        viewFactory(contextHolder.context).displayPrompt(message)
    }

    override fun displayMessage(message: String) {
        viewFactory(contextHolder.context).displayMessage(message)
    }

    override fun displayMenu(menu: MenuView) {
        viewFactory(contextHolder.context).displayMenu(menu)
    }

    override fun displayError(message: String) {
        viewFactory(contextHolder.context).displayError(message)
    }
}