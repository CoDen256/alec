package coden.alec.bot.view.display

import coden.alec.app.views.MenuDisplay
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.context.TelegramChatContext
import coden.alec.bot.context.TelegramMessageContext
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView

class TelegramMenuDisplay(
    private val context: TelegramChatContext,
    private val messageSender: TelegramMessageSender,
    private val menuFormatter: TelegramMenuFormatter
): MenuDisplay {

    override fun displayMenu(menu: MenuView) {
        messageSender.send(context.chatId, menuFormatter.format(menu))
    }
}

class TelegramInlineMenuDisplay(
    private val context: TelegramMessageContext,
    private val messageSender: TelegramMessageSender,
    private val menuFormatter: TelegramMenuFormatter
): MenuDisplay {

    override fun displayMenu(menu: MenuView) {
        messageSender.edit(context.chatId, context.messageId, menuFormatter.format(menu))
    }
}
