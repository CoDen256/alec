package coden.alec.bot.view.proxy

import coden.alec.app.views.MessageDisplay
import coden.alec.bot.context.Context
import coden.alec.bot.context.ContextProxy

open class MessageDisplayContextProxy(contextSupplier: () -> Context, displayFactory: (Context) -> MessageDisplay): ContextProxy<MessageDisplay>(contextSupplier, displayFactory), MessageDisplay {

    override fun displayPrompt(message: String) {
        withContext { displayPrompt(message) }
    }

    override fun displayMessage(message: String) {
        withContext { displayMessage(message) }
    }

    override fun displayError(message: String) {
        withContext { displayError(message) }
    }
}