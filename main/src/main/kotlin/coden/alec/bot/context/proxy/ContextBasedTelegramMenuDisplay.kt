package coden.alec.bot.context.proxy

import coden.alec.app.views.MenuDisplay
import coden.alec.bot.context.Context
import coden.alec.bot.context.ContextProvider
import coden.alec.bot.context.TelegramChatContext
import coden.alec.bot.context.TelegramMessageContext
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.display.TelegramInlineMenuDisplay
import coden.alec.bot.view.display.TelegramMenuDisplay
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView

class ContextBasedTelegramMenuDisplay(
    private val contextSupplier: () -> Context,
    private val messageSenderFactory: (Context) -> TelegramMessageSender,
    private val menuFormatterFactory: (Context) -> TelegramMenuFormatter
) : ContextProvider<MenuDisplay>(), MenuDisplay {

    override fun displayMenu(menu: MenuView) {
        withContext { displayMenu(menu) }
    }

    override fun getContext(): Context {
        return contextSupplier()
    }

    override fun createFromContext(context: Context): MenuDisplay {
        val sender = messageSenderFactory(context)
        val formatter = menuFormatterFactory(context)
        return context.messageId?.let {
            TelegramInlineMenuDisplay(TelegramMessageContext(context.chatId, it), sender, formatter)
        } ?: TelegramMenuDisplay(TelegramChatContext(context.chatId), sender, formatter)
    }
}