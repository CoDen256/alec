package coden.alec.bot.view

import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView

class TelegramMenuView(
    private val context: TelegramChatContext,
    private val messageSender: TelegramMessageSender,
    private val menuFormatter: TelegramMenuFormatter
): coden.alec.app.views.MenuView {

    override fun displayMenu(menu: MenuView) {
        messageSender.send(context.chatId, menuFormatter.format(menu))
    }
}

class TelegramInlineMenuView(
    private val context: TelegramMessageContext,
    private val messageSender: TelegramMessageSender,
    private val menuFormatter: TelegramMenuFormatter
): coden.alec.app.views.MenuView {

    override fun displayMenu(menu: MenuView) {
        messageSender.edit(context.chatId, context.messageId, menuFormatter.format(menu))
    }
}
