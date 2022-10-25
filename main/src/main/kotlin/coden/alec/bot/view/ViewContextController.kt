package coden.alec.bot.view

import coden.alec.app.views.View
import coden.menu.MenuView
import com.github.kotlintelegrambot.Bot

class ViewContextController(private val viewFactory: (Context) -> View): View {

    private lateinit var context: Context

    private val view: View
        get() = viewFactory(context)

    fun updateContext(context: Context){
        this.context = context
    }

    override fun displayPrompt(message: String) {
        view.displayPrompt(message)
    }

    override fun displayMessage(message: String) {
        view.displayMessage(message)
    }

    override fun displayMenu(menu: MenuView) {
        view.displayMenu(menu)
    }

    override fun displayError(message: String) {
        view.displayError(message)
    }
}

class Context (val bot: Bot, val chatId: Long, val messageId: Long?)