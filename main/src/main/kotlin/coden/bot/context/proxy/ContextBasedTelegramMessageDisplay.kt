package coden.bot.context.proxy

import coden.display.displays.MessageDisplay
import coden.bot.context.Context
import coden.bot.context.ContextProvider
import coden.bot.context.TelegramChatContext
import coden.bot.sender.TelegramMessageSender
import coden.bot.view.display.TelegramMessageDisplay

class ContextBasedTelegramMessageDisplay(
    private val contextSupplier: () -> Context,
    private val messageSenderFactory: (Context)-> TelegramMessageSender
):
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
