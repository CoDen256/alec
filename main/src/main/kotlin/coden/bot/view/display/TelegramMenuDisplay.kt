package coden.bot.view.display

import coden.display.displays.MenuDisplay
import coden.bot.sender.TelegramMessageSender
import coden.bot.context.TelegramChatContext
import coden.bot.context.TelegramMessageContext
import coden.bot.view.format.TelegramMenuFormatter
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
