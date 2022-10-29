package coden.bot.context.proxy

import coden.display.displays.MenuDisplay
import coden.bot.context.Context
import coden.bot.context.ContextProvider
import coden.bot.context.TelegramChatContext
import coden.bot.context.TelegramMessageContext
import coden.bot.sender.TelegramMessageSender
import coden.bot.view.display.TelegramInlineMenuDisplay
import coden.bot.view.display.TelegramMenuDisplay
import coden.bot.view.format.TelegramMenuFormatter
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
        return context.messageId?.let { // TODO: Make TelegramInlineMenuDisplay unaware. Just something that accepts MessageContext/ChatContext
            TelegramInlineMenuDisplay(TelegramMessageContext(context.chatId, it), sender, formatter)
        } ?: TelegramMenuDisplay(TelegramChatContext(context.chatId), sender, formatter)
    }
}