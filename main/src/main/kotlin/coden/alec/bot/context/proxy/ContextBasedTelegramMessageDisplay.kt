package coden.alec.bot.context.proxy

import coden.alec.app.views.MessageDisplay
import coden.alec.bot.context.Context
import coden.alec.bot.context.ContextProvider
import coden.alec.bot.context.TelegramChatContext
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.display.TelegramMessageDisplay

class ContextBasedTelegramMessageDisplay(
    private val contextSupplier: () -> Context,
    private val messageSenderFactory: (Context)-> TelegramMessageSender):
    ContextProvider<MessageDisplay>(), MessageDisplay {

    override fun displayPrompt(message: String) {
        withContext { displayPrompt(message) }
    }

    override fun displayMessage(message: String) {
        withContext { displayMessage(message) }
    }

    override fun displayError(message: String) {
        withContext { displayError(message) }
    }

    override fun getContext(): Context {
        return contextSupplier()
    }

    override fun createFromContext(context: Context): MessageDisplay {
        return TelegramMessageDisplay(TelegramChatContext(context.chatId), messageSenderFactory(context))
    }
}
