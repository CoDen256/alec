package coden.alec.bot.view.format

import coden.alec.bot.sender.TelegramMessage
import coden.menu.MenuView

interface TelegramMenuFormatter {
    fun format(menu: MenuView) : TelegramMessage
}