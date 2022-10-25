package coden.alec.bot.view

import coden.alec.app.views.View
import coden.menu.MenuView

class ViewController(
    private val contextHolder: ContextData,
    private val viewFactory: (Context) -> View
): View {

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
    private val viewFactory: () -> coden.alec.app.views.MenuView
): coden.alec.app.views.MenuView {

    override fun displayMenu(menu: MenuView) {
        viewFactory().displayMenu(menu)
    }

}