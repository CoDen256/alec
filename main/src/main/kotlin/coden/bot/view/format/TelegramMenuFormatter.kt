package coden.bot.view.format

import coden.bot.sender.TelegramMessage
import coden.menu.MenuView

interface TelegramMenuFormatter {
    fun format(menu: MenuView) : TelegramMessage
}