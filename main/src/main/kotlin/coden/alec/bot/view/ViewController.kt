package coden.alec.bot.view

import coden.alec.app.views.MessageDisplay
import coden.menu.MenuView

class ViewController(
    private val contextHolder: ContextData,
    private val viewFactory: (Context) -> MessageDisplay
): MessageDisplay {

    override fun displayPrompt(message: String) {
        viewFactory(contextHolder.context).displayPrompt(message)
    }

    override fun displayMessage(message: String) {
        viewFactory(contextHolder.context).displayMessage(message)
    }

    override fun displayError(message: String) {
        viewFactory(contextHolder.context).displayError(message)
    }
}

class MenuViewController(
    private val viewFactory: () -> coden.alec.app.views.MenuDisplay
): coden.alec.app.views.MenuDisplay {

    override fun displayMenu(menu: MenuView) {
        viewFactory().displayMenu(menu)
    }

}