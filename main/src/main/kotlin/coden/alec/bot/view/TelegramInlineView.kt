package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.sender.TelegramMessage
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView

class TelegramInlineView(
    private val context: TelegramMessageContext,
    private val messageSender: TelegramMessageSender,
    private val menuFormatter: TelegramMenuFormatter
): View {
    override fun displayPrompt(message: String) {
        messageSender.edit(context.chatId, context.messageId, TelegramMessage(
            message, null
        ))
    }

    override fun displayMessage(message: String) {
        messageSender.edit(context.chatId, context.messageId, TelegramMessage(
            message, null
        ))
    }

    override fun displayMenu(menu: MenuView) {
        messageSender.edit(context.chatId, context.messageId, menuFormatter.format(
            menu
        ))
    }

    override fun displayError(message: String) {
        messageSender.edit(context.chatId, context.messageId, TelegramMessage(
            message, null
        ))
    }

}
